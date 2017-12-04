package cn.sansotta.market.service.impl

import cn.sansotta.market.common.getBytes
import cn.sansotta.market.common.retry
import cn.sansotta.market.common.toMoneyAmount
import cn.sansotta.market.configuration.AlipayConfiguration
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.domain.value.Trade
import cn.sansotta.market.service.AlipayPaymentService
import cn.sansotta.market.service.CommonPaymentService
import cn.sansotta.market.service.OrderService
import cn.sansotta.market.service.TradeService
import com.alipay.api.AlipayApiException
import com.alipay.api.AlipayClient
import com.alipay.api.internal.util.AlipaySignature
import com.alipay.api.request.AlipayTradeCloseRequest
import com.alipay.api.request.AlipayTradePagePayRequest
import com.alipay.api.request.AlipayTradeQueryRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("pay")
@Service
class AlipayPaymentManager(private val props: AlipayConfiguration.AlipayConfigurationProperties,
                           private val alipayClient: AlipayClient,
                           private val orderService: OrderService,
                           private val paymentService: CommonPaymentService,
                           private val tradeService: TradeService,
                           private val objectMapper: ObjectMapper)
    : AlipayPaymentService {
    private val logger = LoggerFactory.getLogger(AlipayPaymentManager::class.java)

    // this method must be called from CommonPaymentManager.startAlipayPayment to assure concurrent-safety
    @Transactional(propagation = Propagation.MANDATORY)
    override fun startPayment(order: Order): String? {
        val request = AlipayTradePagePayRequest()
        request.returnUrl = props.returnUrl
        request.notifyUrl = props.notifyUrl
        request.bizContent = String(
                objectMapper.writeValueAsString(AlipayPayment(order))
                        .replace(System.lineSeparator(), "").getBytes(Charsets.UTF_8))
        return try {
            val form = alipayClient.pageExecute(request).body

            if (form == null) paymentService.paymentCancel(order.getId())

            form
        } catch (ex: AlipayApiException) {
            logger.error("error when execute payment via alipay", ex)
            paymentService.paymentCancel(order.getId())

            null
        }
    }

    @Throws(RuntimeException::class)
    @Transactional
    override fun handleSuccessNotification(param: Map<String, String>): Boolean {
        if (!checkValid(param)) return false

        val orderId = param["out_trade_no"]!!.toLong()

        if (tradeService.getTradeLocked(orderId) != null) return false

        val order = paymentService.paymentDone(orderId) ?: return false

        // unlike paypal, the trade is created here
        val trade = Trade(order.getId(), "alipay", param["trade_no"]!!, null)
        if (retryNewTrade(trade) && paymentService.paymentDone(order.getId()) != null)
            return true

        // failure will only happen when db down, throw exception to rollback(just placebo)
        throw RuntimeException("Fail to finish trade, maybe database down")
    }

    // this method must be called from CommonPaymentManager.cancelPayment to assure concurrent-safety
    @Transactional(propagation = Propagation.MANDATORY)
    override fun closePayment(oid: Long) {
        val request = AlipayTradeCloseRequest()
        request.bizContent = "{\"out_trade_no\":\"$oid\"}"

        try {
            if (!alipayClient.execute(request).isSuccess)
                throw AlipayApiException("fail to call alipay close payment api")
            // cancellation is allowed only before paying,
            // so as for alipay, no trade record in db, no need to call voidTrade
        } catch (ex: AlipayApiException) {
            // we should get AlipayApiException only when inconsistency between our system and alipay
            // system, so this cancel progress will re-sync, no need to rollback
            val status = checkTradeStatus(oid)
            logger.info("STATUS IS [$status]")
            if (status != "WAIT_BUYER_PAY") return // synced

            throw ex
        } catch (ex: Exception) {
            throw RuntimeException(ex) // throw exception to trigger rollback
        }
    }

    override fun checkSign(params: Map<String, String>) =
            try {
                AlipaySignature.rsaCheckV1(params, props.alipayPublicKey, "UTF-8", "RSA2")
            } catch (ex: AlipayApiException) {
                logger.error("error when check signature via alipay", ex)
                false
            }

    private fun checkValid(params: Map<String, String>)
            = params["seller_id"] == props.sellerId &&
            params["app_id"] == props.appId &&
            params["out_trade_no"]?.toLong()?.let(orderService::getOrderLockedNoItems)?.let {
                (it.status == OrderStatus.PAYING) &&
                        (it.bill.actualTotalPrice == params["total_amount"]?.toDouble())
            } ?: false

    private fun retryNewTrade(trade: Trade) = retry { tradeService.newTrade(trade) }

    private fun checkTradeStatus(orderId: Long): String? {
        if (orderId <= 0) return null
        val query = AlipayTradeQueryRequest()
        query.bizContent = "{\"out_trade_no\":\"$orderId\"}"
        return alipayClient.execute(query).takeIf { it.isSuccess }?.tradeStatus
    }

    data class AlipayPayment(var outTradeNo: String,
                             val productCode: String,
                             var totalAmount: String,
                             val subject: String,
                             val timeoutExpress: String) {
        constructor(order: Order) : this(order.getId().toString(),
                "FAST_INSTANT_TRADE_PAY",
                order.bill.actualTotalPrice.toMoneyAmount(),
                "Sansotta商城订单",
                "3h")
    }
}
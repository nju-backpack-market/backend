package cn.sansotta.market.service.impl

import cn.sansotta.market.common.getBytes
import cn.sansotta.market.common.toMoneyAmount
import cn.sansotta.market.configuration.AlipayConfiguration
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.service.AlipayPaymentService
import cn.sansotta.market.service.OrderService
import com.alipay.api.AlipayApiException
import com.alipay.api.AlipayClient
import com.alipay.api.internal.util.AlipaySignature
import com.alipay.api.request.AlipayTradePagePayRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class AlipayPaymentManager(val props: AlipayConfiguration.AlipayConfigurationProperties,
                           val alipayClient: AlipayClient,
                           val orderService: OrderService,
                           val objectMapper: ObjectMapper)
    : AlipayPaymentService {
    private val logger = LoggerFactory.getLogger(AlipayPaymentManager::class.java)

    override fun createPayment(order: Order): String? {
        val request = AlipayTradePagePayRequest()
        request.returnUrl = props.returnUrl
        request.notifyUrl = props.notifyUrl
        request.bizContent = String(
                objectMapper.writeValueAsString(AlipayPayment(order))
                        .replace(System.lineSeparator(), "").getBytes(Charsets.UTF_8))
        return try {
            alipayClient.pageExecute(request).body
        } catch (ex: AlipayApiException) {
            logger.error("error when execute payment via alipay", ex)
            null
        }
    }

    override fun checkSign(params: Map<String, String>) =
            try {
                AlipaySignature.rsaCheckV1(params, props.alipayPublicKey, "UTF-8", "RSA2")
            } catch (ex: AlipayApiException) {
                logger.error("error when check signature via alipay", ex)
                false
            }

    override fun checkValid(params: Map<String, String>)
            = params["seller_id"] == props.sellerId &&
            params["app_id"] == props.appId &&
            params["out_trade_no"]?.toLong()?.let(orderService::order)?.let {
                (it.status == OrderStatus.PAYING) &&
                        (it.bill.actualTotalPrice == params["total_amount"]?.toDouble())
            } ?: false

    data class AlipayPayment(var outTradeNo: String,
                             var productCode: String,
                             var totalAmount: String,
                             var subject: String) {
        constructor(order: Order) : this(order.getId().toString(),
                "FAST_INSTANT_TRADE_PAY",
                order.bill.actualTotalPrice.toMoneyAmount(),
                "sansotta.cn order")
    }
}
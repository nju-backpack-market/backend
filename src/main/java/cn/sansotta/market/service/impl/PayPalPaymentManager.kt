package cn.sansotta.market.service.impl

import cn.sansotta.market.common.retry
import cn.sansotta.market.common.toMoneyAmount
import cn.sansotta.market.configuration.PayPalApiContextFactory
import cn.sansotta.market.domain.value.DeliveryInfo
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.domain.value.Trade
import cn.sansotta.market.service.CommonPaymentService
import cn.sansotta.market.service.PayPalPaymentService
import cn.sansotta.market.service.ProductService
import cn.sansotta.market.service.TradeService
import com.paypal.api.payments.*
import com.paypal.base.rest.PayPalRESTException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Service
class PayPalPaymentManager(private val factory: PayPalApiContextFactory,
                           private val productService: ProductService,
                           private val paymentService: CommonPaymentService,
                           private val tradeService: TradeService,
                           private val redirect: RedirectUrls) : PayPalPaymentService {
    private val logger = LoggerFactory.getLogger(PayPalPaymentManager::class.java)

    // this method must be called from CommonPaymentManager.startPayPalPayment to assure concurrent-safety
    @Transactional(propagation = Propagation.MANDATORY)
    override fun startPayment(order: Order): String? {
        // actually if only network has no problem, error should never occurs in this method
        // but i'm so scared to deal with payment...
        val payment = createPayment(order)
        if (payment == null) {
            paymentService.paymentCancel(order.getId())
            return null
        }

        // null value shall never exists, unless PayPal has some trouble
        val url = payment.links.find { "approval_url" == it.rel }?.href ?: return null
        val token = extractPayPalToken(url) ?: return null

        // unlike alipay, trade is create before fee being paid
        val trade = Trade(order.getId(), "paypal", payment.id, token)
        return if (retry(5) { tradeService.newTrade(trade) }) url
        else {// this only happens when db down, we return null simply since trade isn't written
            // this may not succeed since db may be down, just try
            // anyway, this doesn't matter since PayPal requires no explicit trade cancellation
            paymentService.paymentCancel(order.getId())
            null
        }
    }

    // this method must be called from CommonPaymentManager.cancelPayment to assure concurrent-safety
    @Transactional(propagation = Propagation.MANDATORY)
    override fun closePayment(orderId: Long) {
        // PayPal tech support said it's not necessary to close trade manually, so just void the record
        // in our db
        tradeService.getTradeLocked(orderId) ?: return
        if (!retry { tradeService.voidTrade(orderId) }) throw RuntimeException("unable void trade")
    }

    private fun convertShoppingList(list: Iterable<ShoppingItem>,
                                    deliveryInfo: DeliveryInfo) =
            ItemList().apply {
                items = list.mapNotNull(this@PayPalPaymentManager::convertShoppingItem)
                shippingAddress = convertDeliveryInfo(deliveryInfo)
            }

    private fun convertShoppingItem(item: ShoppingItem): Item?
            = Item(productService.productName(item.pid)!!,
            item.count.toString(),
            item.actualUnitPrice.toMoneyAmount(),
            "USD")

    private fun convertDeliveryInfo(info: DeliveryInfo) =
            ShippingAddress().apply {
                recipientName = info.name
                phone = info.phoneNumber
                line1 = info.addressLine1
                line2 = info.addressLine2
                postalCode = info.postalCode
                city = info.city
                state = info.province
                countryCode = "C2"
            }

    private fun Order.getAmount() =
            Amount("USD", bill.actualTotalPrice.toMoneyAmount())
                    .apply {
                        details = Details().apply {
                            subtotal = bill.sumByDouble { it.actualSubtotalPrice }.toMoneyAmount()
                        }
                    }


    private val tokenPattern = "token=(EC-[0-9A-Z])&?".toRegex()

    private fun extractPayPalToken(href: String) =
            tokenPattern.find(href)?.let { it.groupValues[0] }

    private fun createPayment(order: Order): Payment? {
        // we trust the pass-in order instance because it is directly from data source
        val payment = Payment().apply {
            intent = "sale"
            redirectUrls = redirect
            payer = Payer().apply { paymentMethod = "paypal" }
            transactions = listOf(
                    Transaction().apply {
                        description = "Sansotta商城订单"
                        amount = order.getAmount()
                        referenceId = order.getId().toString()
                        itemList = convertShoppingList(order.bill, order.deliveryInfo)
                    })
        }
        return try {
            payment.create(factory.apiContext())
        } catch (ex: PayPalRESTException) {
            // never attach here unless network error
            logger.error("error when create payment via PayPal", ex)
            null
        }
    }

    @Transactional
    override fun executePayment(paymentId: String, payerId: String): Payment? {
        tradeService.getTradeByTradeIdLocked(paymentId) ?: return null
        return try { // no need to defense, PayPal rejects invalid ones
            Payment().apply { id = paymentId }
                    .execute(factory.apiContext(),
                            PaymentExecution().apply { this.payerId = payerId })
        } catch (ex: PayPalRESTException) {
            logger.error("error when execute payment via PayPal", ex)
            null
        }
    }
}
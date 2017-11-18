package cn.sansotta.market.service.impl

import cn.sansotta.market.common.retry
import cn.sansotta.market.common.toMoneyAmount
import cn.sansotta.market.configuration.PayPalApiContextFactory
import cn.sansotta.market.domain.value.*
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.service.*
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
                           private val ratingService: ExchangeRatingService,
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
        return if (retry { tradeService.newTrade(trade) }) url
        else {// this only happens when db down, we return null simply since trade isn't written
            // this may not succeed since db may be down, just try
            // anyway, this doesn't matter since PayPal requires no explicit trade cancellation
            paymentService.paymentCancel(order.getId())
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

    // this method must be called from CommonPaymentManager.cancelPayment to assure concurrent-safety
    @Transactional(propagation = Propagation.MANDATORY)
    override fun closePayment(orderId: Long) {
        // PayPal tech support said it's not necessary to close trade manually, so just void the record
        // in our db
        tradeService.getTradeLocked(orderId) ?: return
        if (!retry { tradeService.voidTrade(orderId) }) throw RuntimeException("unable void trade")
    }

    private fun createPayment(order: Order): Payment? {
        // we trust the pass-in order instance because it is directly from data source
        return try {
            val payment = makePayment(order, ratingService.getRating())
            payment.create(factory.apiContext())
        } catch (ex: Exception) {
            // never attach here unless network error
            logger.error("error when create payment via PayPal", ex)
            null
        }
    }


    private val defaultPayer = Payer().apply { paymentMethod = "paypal" }
    private fun makePayment(order: Order, rating: Double): Payment {
        return Payment().apply {
            intent = "sale"
            redirectUrls = redirect
            payer = defaultPayer
            transactions = listOf(makeTransaction(order, rating))
        }
    }

    private fun makeTransaction(order: Order, rating: Double): Transaction {
        return Transaction().apply {
            description = "Sansotta商城订单"
            amount = order.getAmount(rating)
            referenceId = order.getId().toString()
            itemList = makeItemList(order.bill, order.deliveryInfo, rating)
        }
    }

    private fun Order.getAmount(rating: Double) =
            Amount("USD", (bill.actualTotalPrice / rating).toMoneyAmount())
                    .apply { details = Details().apply { subtotal = bill.defaultTotal(rating) } }

    private fun Bill.defaultTotal(rating: Double) = (sumByDouble { it.actualSubtotalPrice } / rating).toMoneyAmount()

    private fun makeItemList(list: Iterable<ShoppingItem>, deliveryInfo: DeliveryInfo, rating: Double) =
            ItemList().apply {
                items = list.map { makeItem(it, rating) }
                shippingAddress = makeShippingAddress(deliveryInfo)
            }

    private fun makeItem(item: ShoppingItem, rating: Double): Item
            = Item(productService.productName(item.pid)!!, "1",
            (item.actualSubtotalPrice / rating).toMoneyAmount(), "USD")

    private fun makeShippingAddress(info: DeliveryInfo) =
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


    private val tokenPattern = "token=(EC-[0-9A-Z]*)&?".toRegex()

    private fun extractPayPalToken(href: String) =
            tokenPattern.find(href)?.let { it.groupValues[1] }
}
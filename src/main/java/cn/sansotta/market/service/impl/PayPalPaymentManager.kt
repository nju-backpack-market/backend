package cn.sansotta.market.service.impl

import cn.sansotta.market.common.toMoneyAmount
import cn.sansotta.market.configuration.PayPalApiContextFactory
import cn.sansotta.market.domain.value.DeliveryInfo
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.service.PayPalPaymentService
import cn.sansotta.market.service.ProductService
import com.paypal.api.payments.*
import com.paypal.base.rest.PayPalRESTException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Service
class PayPalPaymentManager(private val factory: PayPalApiContextFactory,
                           private val productService: ProductService,
                           private val redirect: RedirectUrls) : PayPalPaymentService {
    private val logger = LoggerFactory.getLogger(PayPalPaymentManager::class.java)

    private fun convertShoppingList(list: Iterable<ShoppingItem>,
                                    deliveryInfo: DeliveryInfo) =
            ItemList().apply {
                items = list.map(this@PayPalPaymentManager::convertShoppingItem)
                shippingAddress = convertDeliveryInfo(deliveryInfo)
            }

    private fun convertShoppingItem(item: ShoppingItem)
            = Item("MOCK", //TODO: temporarily mock, product should fetch from productService
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

    override fun createPayment(order: Order): Payment? {
        // we trust the pass-in order instance because it is directly from data source
        val payment = Payment().apply {
            intent = "sale"
            redirectUrls = redirect
            payer = Payer().apply { paymentMethod = "paypal" }
            transactions = listOf(
                    Transaction().apply {
                        description = "Sansotta订单"
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

    override fun executePayment(paymentId: String, payerId: String) =
            try {
                Payment().apply { id = paymentId }
                        .execute(factory.apiContext(),
                                PaymentExecution().apply { this.payerId = payerId })
            } catch (ex: PayPalRESTException) {
                logger.error("error when execute payment via PayPal", ex)
                null
            }
}
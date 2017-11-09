package cn.sansotta.market.service.impl

import cn.sansotta.market.configuration.PayPalApiContextFactory
import cn.sansotta.market.domain.value.DeliveryInfo
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.service.PaymentService
import cn.sansotta.market.service.ProductService
import com.paypal.api.payments.*
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class PayPalPaymentManager(private val factory: PayPalApiContextFactory,
                           private val productService: ProductService) : PaymentService {
    private fun convertShoppingList(list: Iterable<ShoppingItem>,
                                    deliveryInfo: DeliveryInfo) =
            ItemList().apply {
                items = list.map(this@PayPalPaymentManager::convertShoppingItem)
                shippingAddress = convertDeliveryInfo(deliveryInfo)
            }

    private fun convertShoppingItem(item: ShoppingItem)
            = Item("MOCK",
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

    private fun Double.toMoneyAmount() = String.format("%.2f", this)

    private fun Order.getAmount() =
            Amount("USD", bill.actualTotalPrice.toMoneyAmount())
                    .apply {
                        details = Details().apply {
                            subtotal = bill.sumByDouble { it.actualSubtotalPrice }.toMoneyAmount()
                        }
                    }

    override fun createPayment(order: Order, returnUrl: String, cancelUrl: String): Payment? {
        val payment = Payment().apply {
            intent = "sale"
            payer = Payer().apply { paymentMethod = "paypal" }
            transactions = listOf(
                    Transaction().apply {
                        description = "Order from sansotta.cn"
                        amount = order.getAmount()
                        referenceId = order.getId().toString()
                        itemList = convertShoppingList(order.bill, order.deliveryInfo)
                    })
            redirectUrls = RedirectUrls().apply {
                this.cancelUrl = cancelUrl
                this.returnUrl = returnUrl
            }
        }
        return payment.create(factory.apiContext())
    }

    override fun executePayment(paymentId: String, payerId: String): Payment? {
        return Payment().apply { id = paymentId }
                .execute(factory.apiContext(), PaymentExecution().apply { this.payerId = payerId })
    }
}
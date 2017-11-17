package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import com.paypal.api.payments.Payment

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface PayPalPaymentService {
    fun startPayment(order: Order): String?
    fun closePayment(orderId: Long)
    fun executePayment(paymentId: String, payerId: String): Payment?
}
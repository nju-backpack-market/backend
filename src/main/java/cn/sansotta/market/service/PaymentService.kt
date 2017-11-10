package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import com.paypal.api.payments.Payment

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface PaymentService {
    fun createPayment(order: Order): Payment?

    fun executePayment(paymentId: String, payerId: String): Payment?
}
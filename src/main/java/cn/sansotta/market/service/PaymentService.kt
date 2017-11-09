package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import com.paypal.api.payments.Payment
import com.paypal.base.rest.PayPalRESTException

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface PaymentService {
    @Throws(PayPalRESTException::class)
    fun createPayment(order: Order, returnUrl: String, cancelUrl: String): Payment?

    fun executePayment(paymentId: String, payerId: String): Payment?
}
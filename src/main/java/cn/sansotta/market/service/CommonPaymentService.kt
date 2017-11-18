package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface CommonPaymentService {
    fun paymentBegin(orderId: Long): Order?
    fun paymentCancel(orderId: Long): Order?
    fun checkedPaymentCancel(orderId: Long, name: String, phone: String): Order?
    fun paymentDone(orderId: Long): Order?
    fun startAlipayPayment(orderId: Long): String?
    fun startPayPalPayment(orderId: Long): String?
}
package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface AlipayPaymentService {
    fun createPayment(order: Order): String?
    fun checkSign(params: Map<String, String>): Boolean
    fun checkValid(params: Map<String, String>): Boolean
}
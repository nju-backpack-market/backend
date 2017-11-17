package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface AlipayPaymentService {
    fun startPayment(order: Order): String?
    fun checkSign(params: Map<String, String>): Boolean
    fun closePayment(oid: Long)
    @Throws(RuntimeException::class)
    fun handleSuccessNotification(param: Map<String, String>): Boolean
}
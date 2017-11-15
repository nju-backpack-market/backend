package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface MailService {
    fun sendStockOutMail(order: Order):Boolean
}
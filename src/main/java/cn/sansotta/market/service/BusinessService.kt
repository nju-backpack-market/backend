package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Bill

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface BusinessService {
    fun getTotalPrice(bill: Bill): Bill
}
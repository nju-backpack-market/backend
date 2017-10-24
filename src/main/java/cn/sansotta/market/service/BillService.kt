package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.domain.value.ShoppingItem

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface BillService {
    fun queryPrice(items: List<ShoppingItem>): Bill
}
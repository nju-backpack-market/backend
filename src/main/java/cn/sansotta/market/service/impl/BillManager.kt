package cn.sansotta.market.service.impl

import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.service.BillService
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class BillManager : BillService {
    override fun billOfOrder(oid: Long) = Bill.mockObject()

    override fun queryPrice(items: Iterable<ShoppingItem>)
            = Bill(items.map { it.originUnitPrice = 1919.0;it })
}
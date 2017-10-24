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
    override fun queryPrice(items: List<ShoppingItem>) = Bill(items)
}
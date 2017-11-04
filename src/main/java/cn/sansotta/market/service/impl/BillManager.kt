package cn.sansotta.market.service.impl

import cn.sansotta.market.dao.ProductDao
import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.service.BillService
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class BillManager(private val productDao: ProductDao) : BillService {
    override fun billOfOrder(oid: Long) = Bill.mockObject()

    override fun queryPrice(items: Iterable<ShoppingItem>): Bill? {
        if (!items.all { it.pid > 0L }) return null

        for (item in items)
            productDao.selectProductById(item.pid)
                    ?.let { item.originUnitPrice = it.price } ?: return null
        return Bill(items)
    }

    override fun checkPrice(bill: Bill)
            = bill.all {
        it.originUnitPrice >= 0.0 && // do mostly check before query db
                it.originUnitPrice == productDao.selectProductById(it.pid)?.price
    }
}
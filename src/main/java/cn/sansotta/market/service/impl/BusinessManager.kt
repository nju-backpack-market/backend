package cn.sansotta.market.service.impl

import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.service.BusinessService
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class BusinessManager : BusinessService {
    override fun getTotalPrice(bill: Bill): Bill {
        bill.forEachIndexed { idx, item ->
            item.apply {
                originUnitPrice = (idx + 1) * 2.0
                actualUnitPrice = originUnitPrice - 1
                actualSubtotalPrice = defaultSubtotalPrice
            }
        }
        return bill
    }
}
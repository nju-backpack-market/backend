package cn.sansotta.market.service.impl

import cn.sansotta.market.domain.Bill
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
                originalUnitPrice = (idx + 1) * 2
                actualUnitPrice = originalUnitPrice - 1
                actualSubtotalPrice = defaultSubtotalPrice
                unitPriceDiscountReason = "For Test"
            }
        }
        return bill
    }
}
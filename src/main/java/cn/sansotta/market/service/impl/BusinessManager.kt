package cn.sansotta.market.service.impl

import cn.sansotta.market.domain.ShoppingList
import cn.sansotta.market.service.BusinessService
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class BusinessManager : BusinessService {
    override fun getTotalPrice(shoppingList: ShoppingList): ShoppingList {
        shoppingList.list.
                forEachIndexed { idx, item -> item.unitPrice = (idx + 1) * 2;item.subtotalPrice = item.unitPrice * item.count }
        return shoppingList
    }
}
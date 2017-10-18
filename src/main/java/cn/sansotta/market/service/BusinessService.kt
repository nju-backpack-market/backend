package cn.sansotta.market.service

import cn.sansotta.market.domain.ShoppingList

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface BusinessService {
    fun getTotalPrice(shoppingList: ShoppingList): ShoppingList
}
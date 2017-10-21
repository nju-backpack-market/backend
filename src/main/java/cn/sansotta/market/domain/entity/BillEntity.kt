package cn.sansotta.market.domain.entity

import cn.sansotta.market.domain.Price
import cn.sansotta.market.domain.ShoppingItem

/**
 * Po for Bill.
 *
 * Table: bill(oid, total_origin, total_actual, total_discount)
 *
 * [totalPrice] is a simple wrapper.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class BillEntity(
        val orderId: Int,
        val shoppingList: List<ShoppingItem>,
        val totalPrice: Price
)
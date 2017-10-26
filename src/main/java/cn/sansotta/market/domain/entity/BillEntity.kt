package cn.sansotta.market.domain.entity

/**
 * Po for Bill.
 *
 * [totalPrice] is a simple wrapper.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class BillEntity(
        val shoppingList: List<ShoppingItemEntity>,
        val totalPrice: Double?
)
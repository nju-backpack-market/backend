package cn.sansotta.market.domain

/**
 * Shopping item in shopping list.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class ShoppingItem(
        /**
         * Id of production.
         * */
        var pid: Int,
        /**
         * Count of production.
         * */
        var count: Int,
        /**
         * Unit price of each.
         * */
        var unitPrice: Int,
        /**
         * Subtotal price of this item.
         *
         * Note that it can differ from count * unitPrice,
         * because of sale promotion strategy.
         * */
        var subtotalPrice: Int
) {
    constructor() : this(0, 0, 0, 0)
}
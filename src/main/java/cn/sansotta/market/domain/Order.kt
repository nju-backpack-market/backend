package cn.sansotta.market.domain

/**
 * Order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class Order @JvmOverloads constructor(
        /**
         * Order's id.
         * */
        var id: Int,
        /**
         * Shopping list of order.
         * */
        var shoppingList: ShoppingList,
        /**
         * Final price of order.
         *
         * It can differ from the total price of shopping list, for some promotion strategy.
         * */
        var strikePrice: Int,
        /**
         * Order's state.
         * */
        var state: OrderState,
        /**
         * Delivery information of order.
         * */
        var deliveryInfo: DeliveryInfo
) {
    constructor() : this(-1, ShoppingList(), 0, OrderState.CREATED, DeliveryInfo())
}

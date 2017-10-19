package cn.sansotta.market.domain

/**
 * Order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class Order(
        /**
         * Order's id.
         * */
        var id: Int,
        /**
         * Shopping list of order.
         * */
        var shoppingList: Bill,
        /**
         * Order's state.
         * */
        var state: OrderState,
        /**
         * Delivery information of order.
         * */
        var deliveryInfo: DeliveryInfo
) {
    constructor() : this(-1, Bill(), OrderState.CREATED, DeliveryInfo())
}

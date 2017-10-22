package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.OrderEntity

/**
 * Order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class Order(
        /**
         * Order's id.
         * */
        var id: Long,
        /**
         * Bill of order.
         * */
        var bill: Bill,
        /**
         * Order's state.
         * */
        var state: OrderState,
        /**
         * Delivery information of order.
         * */
        var deliveryInfo: DeliveryInfo
) : ValueObject<OrderEntity> {
    constructor() : this(-1, Bill(), OrderState.CREATED, DeliveryInfo())

    constructor(po: OrderEntity) : this(po.id, Bill(po.bill), po.state, DeliveryInfo(po.deliveryInfo))

    override fun toEntity() = OrderEntity(id, bill.toEntity(), state, deliveryInfo.toEntity())
}

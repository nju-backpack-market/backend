package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.OrderEntity
import java.time.LocalDateTime

/**
 * Order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
open class Order(
        /**
         * Order's id.
         * */
        var id: Long,
        /**
         * Order's state.
         * */
        var state: OrderState,
        /**
         * Create time of order.
         * */
        var time: LocalDateTime,
        /**
         * Delivery information of order.
         * */
        var deliveryInfo: DeliveryInfo,
        /**
         * Bill of order.
         * */
        var bill: Bill
) : ValueObject<OrderEntity> {
    constructor() : this(-1, OrderState.CREATE, LocalDateTime.now(), DeliveryInfo(), Bill())

    constructor(po: OrderEntity)
            : this(po.id, po.state, po.time, DeliveryInfo(po.deliveryInfo), Bill(po.bill))

    override fun toEntity() = OrderEntity(id, state, time, deliveryInfo.toEntity(), bill.toEntity())
}

package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.OrderEntity
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.Identifiable
import java.time.LocalDateTime

/**
 * Order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.ANY)
data class Order(
        /**
         * Order's id.
         * */
        @get:JvmName("getOid")
        @get:JsonIgnore
        var id: Long,
        /**
         * Order's state.
         * */
        @set:JsonIgnore
        @get:JsonProperty
        var status: OrderStatus,
        /**
         * Create time of order.
         * */
        @set:JsonIgnore
        @get:JsonProperty
        var time: LocalDateTime,
        /**
         * Delivery information of order.
         * */
        var deliveryInfo: DeliveryInfo,
        /**
         * Bill of order.
         * */
        @get:JsonIgnore
        @set:JsonProperty
        var bill: Bill
) : ValueObject<OrderEntity>, Identifiable<Long> {
    override fun getId(): Long {
        return id
    }

    constructor() : this(-1, OrderStatus.INVALID, LocalDateTime.now(), DeliveryInfo(), Bill())

    constructor(po: OrderEntity)
            : this(po.id, po.status, po.time, DeliveryInfo(po.deliveryInfo),
            Bill(po.shoppingItems.map { ShoppingItem(it) }).apply { actualTotalPrice = po.totalPrice })

    override fun toEntity() =
            OrderEntity(id, bill.actualTotalPrice, status, time,
                    deliveryInfo.toEntity(),
                    bill.map { it.toEntity() })

    companion object {
        @JvmStatic
        fun mockObject() = Order(1, OrderStatus.CREATE, LocalDateTime.now(), DeliveryInfo.mockObject(),
                Bill.mockObject())

        @JvmStatic
        fun isValidEntity(order: Order)
                = DeliveryInfo.isValidEntity(order.deliveryInfo) &&
                Bill.isValidEntity(order.bill) &&
                order.time.isBefore(LocalDateTime.now())

        @JvmStatic
        fun mergeAsUpdate(origin: OrderEntity, modified: Order): Order? {
            // only allowed modified origin delivery info and status
            if (origin.id != modified.id) return null

            if (modified.status == OrderStatus.INVALID)
                modified.status = origin.status
            modified.time = origin.time
            modified.deliveryInfo =
                    DeliveryInfo.mergeAsUpdate(origin.deliveryInfo, modified.deliveryInfo)
            return modified
        }
    }
}

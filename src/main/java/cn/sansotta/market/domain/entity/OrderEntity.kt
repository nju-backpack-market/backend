package cn.sansotta.market.domain.entity

import cn.sansotta.market.domain.value.OrderState
import org.apache.ibatis.type.BaseTypeHandler
import java.time.LocalDateTime

/**
 * Po for Order.
 *
 * Table: order_t(id, state, c_name, c_phoneNumber, c_email, c_address)
 *
 * [state] is handled by a customized [BaseTypeHandler].
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class OrderEntity(
        val id: Long,
        val totalPrice: Double?,
        val state: OrderState,
        val time: LocalDateTime,
        val deliveryInfo: DeliveryInfoEntity,
        val shoppingItems: List<ShoppingItemEntity>
)

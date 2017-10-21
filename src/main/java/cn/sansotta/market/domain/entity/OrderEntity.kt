package cn.sansotta.market.domain.entity

import cn.sansotta.market.domain.OrderState
import org.apache.ibatis.type.BaseTypeHandler

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
        var id: Long,
        var shoppingList: BillEntity,
        var state: OrderState,
        var deliveryInfo: DeliveryInfoEntity
)
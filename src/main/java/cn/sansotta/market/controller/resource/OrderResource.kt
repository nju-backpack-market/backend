package cn.sansotta.market.controller.resource

import cn.sansotta.market.domain.value.Order
import org.springframework.hateoas.Resource

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class OrderResource(order: Order) : Resource<Order>(order) {
    val bill = BillResource(order.bill, false)
}
package cn.sansotta.market.service

import cn.sansotta.market.controller.resource.OrderQuery
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderState
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface OrderService {
    fun order(id: Long): Order?
    fun allOrders(page: Int): PageInfo<Order>?
    fun allOrdersIndex(page: Int): PageInfo<Order>?
    fun queryOrders(queryId: Int): PageInfo<Order>?
    fun newOrder(order: Order): Order?
    fun modifyOrders(orders: List<Order>): List<Order>?
    fun modifyOrderStatus(id: Long, state: OrderState): Order?

    fun createOrderQuery(query: OrderQuery): OrderQuery?
}
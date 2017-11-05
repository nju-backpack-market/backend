package cn.sansotta.market.service

import cn.sansotta.market.controller.resource.OrderQuery
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface OrderService {
    fun order(id: Long): Order?
    fun allOrders(page: Int): PageInfo<Order>?
    fun allOrdersIndex(page: Int): PageInfo<Order>?
    fun newOrder(order: Order): Order?
    fun modifyOrders(orders: List<Order>): List<Order>
    fun modifyOrderStatus(id: Long, status: OrderStatus): Order?

    fun createOrderQuery(query: OrderQuery): OrderQuery?
    fun getOrderQuery(queryId: Int): OrderQuery?
    fun queryOrders(query: OrderQuery): PageInfo<Order>?
}
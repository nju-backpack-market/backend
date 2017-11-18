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
    fun getOrderLocked(id: Long): Order?
    fun getOrderLockedNoItems(id: Long): Order?
    fun allOrders(page: Int, full: Boolean): PageInfo<Order>?
    fun newOrder(order: Order): Order?
    fun modifyOrders(orders: List<Order>): List<Order>
    fun modifyOrderStatus(id: Long, status: OrderStatus): Order?

    fun createOrderQuery(query: OrderQuery, authorized: Boolean): OrderQuery?
    fun getOrderQuery(queryId: Int): OrderQuery?
    fun queryOrders(page: Int, query: OrderQuery): PageInfo<Order>?
}
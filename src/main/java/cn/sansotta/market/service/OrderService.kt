package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderState
import com.github.pagehelper.PageInfo
import java.time.LocalDate

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface OrderService {
    fun order(id: Long): Order?
    fun ordersOfCustomer(name: String, phoneNumber: String): List<Order>?
    fun ordersOfStatus(state: OrderState): List<Order>?
    fun ordersOfDate(date: LocalDate): List<Order>?
    fun allOrders(page: Int): PageInfo<Order>?
    fun allOrdersIndex(page: Int): PageInfo<Order>?
    fun newOrder(order: Order): Order?
    fun modifyOrders(orders: List<Order>): List<Order>?
    fun modifyOrderStatus(id: Long, state: OrderState): Order?
}
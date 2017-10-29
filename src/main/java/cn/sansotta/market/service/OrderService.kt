package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Order
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface OrderService {
    fun order(id: Long): Order?
    fun createOrder(order: Order): Order?
    fun allOrders(page: Int): PageInfo<Order>?
    fun allOrdersIndex(page: Int): PageInfo<Order>?
}
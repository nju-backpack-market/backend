package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.controller.resource.OrderQuery
import cn.sansotta.market.dao.OrderDao
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.service.BillService
import cn.sansotta.market.service.OrderService
import com.github.pagehelper.PageInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class OrderManager(private val billService: BillService, private val orderDao: OrderDao) : OrderService {
    private val logger = LoggerFactory.getLogger(OrderManager::class.java)

    override fun order(id: Long)
            = id.takeIf { it > 0L }
            ?.let(orderDao::selectOrderById)
            ?.let(::Order)

    override fun allOrders(page: Int)
            = page.takeIf { it >= 0 }
            ?.let(orderDao::selectAllOrders)
            ?.let { copyPageInfo(it, ::Order) }

    override fun newOrder(order: Order): Order? {
        if (!Order.isValidEntity(order)) return null
        if (!billService.checkPrice(order.bill)) return order.apply { id = -2 } // recheck price

        order.status = OrderStatus.CREATE
        return hazard("create order") { orderDao.insertOrder(order.toEntity()) }
                ?.let { entity -> order.id = entity.id;order } ?: order.apply { id = -1 }
    }

    //    @CachePut("queryCache", key = "#{orderQuery.getQueryId()}", condition = "#{orderQuery!=null}")
    override fun createOrderQuery(query: OrderQuery): OrderQuery? {
        return query.apply { id = 1 }
    }

    //    @Cacheable("queryCache", key = "#{queryId}")
    override fun queryOrders(queryId: Int): PageInfo<Order>? {
        return copyPageInfo(PageInfo(listOf(1L, 2L, 3L))) { Order().apply { id = it } }
    }

    @Transactional
    override fun modifyOrderStatus(id: Long, status: OrderStatus): Order? {
        if (id <= 0L) return null

        val origin = orderDao.selectOrderById(id)?.let(::Order) ?: return null
        if (!origin.status.isValidTransfer(status)) return null

        if (hazard("update order", false) { orderDao.updateOrderStatus(id, status) })
            return origin.apply { this.status = status }
        return origin.apply { this.id = -1 }
    }

    @Transactional
    override fun modifyOrders(orders: List<Order>): List<Order> {
        val valid = orders.filter { it.getId() > 0L }
        return valid.map { orderDao.selectOrderById(it.getId()) }
                .zip(valid)
                .filter { (origin, _) -> origin != null }
                .mapNotNull { (origin, modified) -> Order.mergeAsUpdate(origin, modified) }
                .filter { hazard("update order", false) { orderDao.updateOrder(it.toEntity()) } }
    }

    override fun allOrdersIndex(page: Int): PageInfo<Order>? {
        return copyPageInfo(PageInfo(listOf(1L, 2L, 3L))) { Order().apply { id = it } }
    }

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                null
            }
}
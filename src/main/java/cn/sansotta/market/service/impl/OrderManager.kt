package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.controller.resource.OrderQuery
import cn.sansotta.market.dao.OrderDao
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.service.BillService
import cn.sansotta.market.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ThreadLocalRandom

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
@CacheConfig(cacheNames = arrayOf("queryCache"))
class OrderManager(private val billService: BillService,
                   private val orderDao: OrderDao) : OrderService {

    private val logger = LoggerFactory.getLogger(OrderManager::class.java)
    private val random = ThreadLocalRandom.current()

    override fun order(id: Long)
            = id.takeIf { it > 0L }
            ?.let(orderDao::selectOrderById)
            ?.let(::Order)

    override fun getOrderLocked(id: Long)
            = id.takeIf { it > 0L }
            ?.let(orderDao::selectOrderByIdLocked)
            ?.let(::Order)

    override fun allOrders(page: Int, full: Boolean)
            = page.takeIf { it >= 0 }
            ?.let { orderDao.selectAllOrders(page, full) }
            ?.let { copyPageInfo(it, ::Order) }

    override fun newOrder(order: Order): Order? {
        if (!Order.isValidEntity(order)) return null
        if (!billService.checkPrice(order.bill)) return order.apply { id = -2 } // recheck price

        order.status = OrderStatus.CREATE
        return hazard("create order") { orderDao.insertOrder(order.toEntity()) }
                ?.let { entity -> order.id = entity.id;order } ?: order.apply { id = -1 }
    }

    @CachePut(key = "#result.getQueryId()", condition = "#result != null")
    override fun createOrderQuery(query: OrderQuery, authorized: Boolean): OrderQuery? {
        query.queryId = random.nextInt()
        return query.getRationalQuery(authorized)
    }

    @Cacheable(key = "#p0")
    override fun getOrderQuery(queryId: Int) = null

    override fun queryOrders(page: Int, query: OrderQuery)
            = page.takeIf { it >= 0 }
            ?.let { orderDao.selectOrdersByQuery(it, query) }
            ?.let { copyPageInfo(it, ::Order) }

    @Transactional
    override fun modifyOrderStatus(id: Long, status: OrderStatus): Order? {
        if (id <= 0L) return null

        val origin = orderDao.selectOrderByIdLocked(id)?.let(::Order) ?: return null
        if (!origin.status.canTransferTo(status)) return null

        if (hazard("update order", false) { orderDao.updateOrderStatus(id, status) })
            return origin.apply { this.status = status }
        return origin.apply { this.id = -1 }
    }

    @Transactional
    override fun modifyOrders(orders: List<Order>): List<Order> {
        val valid = orders.filter { it.getId() > 0L }
        return valid.map { orderDao.selectOrderByIdLocked(it.getId()) }
                .zip(valid)
                .filter { (origin, _) -> origin != null }
                .mapNotNull { (origin, modified) -> Order.mergeAsUpdate(origin, modified) }
                .filter { hazard("update order", false) { orderDao.updateOrder(it.toEntity()) } }
    }

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                null
            }
}
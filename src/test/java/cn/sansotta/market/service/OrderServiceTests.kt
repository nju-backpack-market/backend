package cn.sansotta.market.service

import cn.sansotta.market.controller.resource.OrderQuery
import cn.sansotta.market.domain.value.DeliveryInfo
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.domain.value.ShoppingItem
import com.github.pagehelper.PageInfo
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev_local")
@Transactional
@SqlConfig(encoding = "UTF8")
@Sql("classpath:test_schema.sql", "classpath:products_data.sql", "classpath:orders_data.sql")
class OrderServiceTests : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    lateinit var service: OrderService

    @Test
    fun order() {
        assertNull(service.order(-1))

        val order = service.order(1)
        assertNotNull(order)
        order as Order
        assertEquals(110.0, order.bill.originTotalPrice, 0.01)
        assertEquals(109.0, order.bill.actualTotalPrice, 0.01)
        assertEquals("foo1", order.deliveryInfo.name)
        assertEquals(2, order.bill.toList().size)
    }

    @Test
    fun allOrders() {
        assertNull(service.allOrders(-1))

        val orders = service.allOrders(0)
        assertNotNull(orders)
        orders as PageInfo<Order>
        assertEquals(5, orders.size)
    }

    @Test
    fun newOrders() {
        val order = Order()
        Thread.sleep(100) // to make sure LocalDateTime.now() has changed
        assertNull(service.newOrder(order))

        order.deliveryInfo = DeliveryInfo("TEST", "233333", "a@b.com", "China", "JiangSu",
                "Nanjing", "NJU", "EDU", "222222")
        order.bill.addShoppingItem(ShoppingItem(1L, 1, 11.4))
        order.bill.addShoppingItem(ShoppingItem(2L, 2, 51.4))
        val result = service.newOrder(order)
        assertNotNull(result)
        result as Order
        print(result.getId())
        assertTrue(result.getId() > 0L)

        order.id = -1L
        order.bill.addShoppingItem(ShoppingItem(3L, 3, 0.01))
        assertEquals(-2L, service.newOrder(order)?.getId())

        order.bill.addShoppingItem(ShoppingItem(2L, 3, 51.4))
        assertNull(service.newOrder(order))
    }

    @Test
    fun updateOrderStatus() {
        val order = service.modifyOrderStatus(1L, OrderStatus.STOCK_OUT)
        assertNotNull(order)
        order as Order
        assertEquals(1L, order.getId())
        assertEquals(OrderStatus.STOCK_OUT, order.status)
        assertEquals(OrderStatus.STOCK_OUT, service.order(1L)?.status)

        assertNull(service.modifyOrderStatus(1L, OrderStatus.CREATE))
        assertNull(service.modifyOrderStatus(0L, OrderStatus.STOCK_OUT))
        assertNull(service.modifyOrderStatus(1000L, OrderStatus.STOCK_OUT))
    }

    @Test
    fun updateOrderInfo() {
        val updateInfo0 = Order().apply {
            id = 1L
            time = LocalDateTime.MAX
            deliveryInfo.apply {
                name = "zhangsan"
                phoneNumber = "777777"
                email = "foo@bar.com"
                addressLine2 = "PKU"
            }
        }
        val updateInfo1 = Order().apply { id = -1L }
        val updateInfo2 = Order().apply { id = 1000L }
        val updated = service.modifyOrders(listOf(updateInfo0, updateInfo1, updateInfo2))
        assertEquals(1, updated.size)
        assertEquals("zhangsan", updated[0].deliveryInfo.name)
        assertNotEquals(LocalDateTime.MAX, updated[0].time)
        assertEquals("777777", service.order(1)?.deliveryInfo?.phoneNumber)
    }

    @Test
    fun createQuery() {
        val query = OrderQuery()
        query.id = 1
        var res = service.createOrderQuery(query, true)
        assertNotNull(res)
        res as OrderQuery
        assertEquals(query.hashCode(), res.queryId)
        res = service.getOrderQuery(res.queryId)
        assertNotNull(res)
        res as OrderQuery
        assertEquals(query.hashCode(), res.queryId)

        query.id = null
        query.fromDate = LocalDate.MAX
        query.toDate = LocalDate.MIN
        assertNull(service.createOrderQuery(query, true))

        query.fromDate = null
        query.toDate = LocalDate.now()
        query.onDate = LocalDate.now().minusDays(1)
        assertNull(service.createOrderQuery(query, true))

        query.toDate = null
        query.onDate = null
        query.fromPrice = 10.1
        query.toPrice = 10.0
        assertNull(service.createOrderQuery(query, true))
    }

    @Test
    fun queryOrder() {
        val query = OrderQuery()
        query.id = 1
        assertEquals(1, service.queryOrders(0, query)?.size)

        query.id = null
        query.fromPrice = 10.0
        query.toPrice = 100.0
        assertEquals(4, service.queryOrders(0, query)?.size)

        query.productIds = listOf(1, 2)
        assertEquals(2, service.queryOrders(0, query)?.size)
    }

    @Test
    fun paymentBegin() {
        val order = service.paymentBegin(3)
        assertNotNull(order)
        assertEquals(3, order!!.getId())
        assertEquals(OrderStatus.PAYING, order.status)

        assertNull(service.paymentBegin(1))
        assertEquals(OrderStatus.PAYING, service.order(3)!!.status)
    }

    @Test
    fun paymentCancel() {
        val order = service.paymentCancel(1)
        assertNotNull(order)
        assertEquals(1, order!!.getId())
        assertEquals(OrderStatus.CREATE, order.status)

        assertNull(service.paymentCancel(3))
        assertEquals(OrderStatus.CREATE, service.order(1)!!.status)
    }

    @Test
    fun paymentDone() {
        val order = service.paymentDone(1)
        assertNotNull(order)
        assertEquals(1, order!!.getId())
        assertEquals(OrderStatus.STOCK_OUT, order.status)

        assertNull(service.paymentDone(3))
        assertEquals(OrderStatus.STOCK_OUT, service.order(1)!!.status)
    }
}

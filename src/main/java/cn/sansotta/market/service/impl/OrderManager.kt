package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderState
import cn.sansotta.market.service.BillService
import cn.sansotta.market.service.OrderService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class OrderManager(private val billService: BillService) : OrderService {
    override fun ordersOfCustomer(name: String, phoneNumber: String): List<Order>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ordersOfStatus(state: OrderState): List<Order>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ordersOfDate(date: LocalDate): List<Order>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyOrderStatus(id: Long, state: OrderState): Order? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun modifyOrders(orders: List<Order>): List<Order>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allOrdersIndex(page: Int): PageInfo<Order>? {
        return copyPageInfo(PageInfo(listOf(1L, 2L, 3L))) { Order().apply { id = it } }
    }

    override fun order(id: Long): Order? {
        return Order.mockObject().apply { this.id = id }
    }

    override fun newOrder(order: Order): Order? {
        println(order)
        if(!Order.isValidEntity(order)) return null

        //TODO: add really implementation of creating order
        val actualBill = billService.queryPrice(order.bill)
        actualBill.actualTotalPrice = order.bill.actualTotalPrice // mock
        if (actualBill != order.bill)
            return null
        // TODO: add database save logic here
        // mock implementation
        order.id = 1
        order.state = OrderState.CREATE
        return order
    }

    override fun allOrders(page: Int)
            = copyPageInfo(PageInfo(listOf(Order.mockObject().toEntity(),
            Order.mockObject().copy(id = 8888).toEntity()))) { Order(it) }
}
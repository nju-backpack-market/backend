package cn.sansotta.market.service.impl

import cn.sansotta.market.common.retry
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.service.AlipayPaymentService
import cn.sansotta.market.service.CommonPaymentService
import cn.sansotta.market.service.OrderService
import cn.sansotta.market.service.PayPalPaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("pay")
@Service
class CommonPaymentManager(private val orderService: OrderService) : CommonPaymentService {
    private lateinit var payPalService: PayPalPaymentService
    private lateinit var alipayService: AlipayPaymentService

    @Autowired
    @Lazy
    fun setPayPalService(payPalService: PayPalPaymentService) {
        this.payPalService = payPalService
    }

    @Autowired
    @Lazy
    fun setAlipayService(alipayService: AlipayPaymentService) {
        this.alipayService = alipayService
    }

    @Transactional
    override fun paymentBegin(orderId: Long): Order? {
        // trade via alipay has no enough info to init here, so create trade record is delayed
        return orderService.modifyOrderStatus(orderId, OrderStatus.PAYING) ?: return null
    }

    @Transactional
    override fun paymentCancel(orderId: Long): Order? {
        // change order's status first
        val modified = retry(judge = { o -> o.getId() != -1L }) {
            orderService.modifyOrderStatus(orderId, OrderStatus.CREATE) ?: return null
        } ?: return null

        // just avoid to couple with TradeService, let *PaymentService do the work, it shall succeed anyway
        payPalService.closePayment(orderId)
        alipayService.closePayment(orderId)

        return modified
    }

    @Transactional
    override fun checkedPaymentCancel(orderId: Long, name: String, phone: String): Order? {
        val info = orderService.getOrderLockedNoItems(orderId)?.deliveryInfo ?: return null
        if (info.name != name || info.phoneNumber != phone) return null
        return paymentCancel(orderId)
    }

    @Transactional
    override fun paymentDone(orderId: Long): Order? {
        val modified = retry(5, { o -> o.getId() != -1L }) {
            orderService.modifyOrderStatus(orderId, OrderStatus.STOCK_OUT) ?: return null
        } ?: return null
        // TODO: add email notification here

        return modified
    }

    @Transactional
    override fun startAlipayPayment(orderId: Long): String? {
        val order = orderService.getOrderLocked(orderId) ?: return null // lock from here
        return alipayService.startPayment(order)
    }

    @Transactional
    override fun startPayPalPayment(orderId: Long): String? {
        val order = orderService.getOrderLocked(orderId) ?: return null // lock from here
        return payPalService.startPayment(order)
    }
}
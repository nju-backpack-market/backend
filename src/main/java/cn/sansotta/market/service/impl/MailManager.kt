package cn.sansotta.market.service.impl

import cn.sansotta.market.common.commonPool
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.ShoppingItem
import cn.sansotta.market.service.MailService
import cn.sansotta.market.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
//@Service
class MailManager(private val sender: MailSender,
                  private val productService: ProductService) : MailService {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun sendStockOutMail(order: Order): Boolean {
        val message = newMessage(order) ?: return false
        commonPool.submit { retrySend(message) }
        return true
    }

    private fun retrySend(message: SimpleMailMessage) {
        while (true) {
            try {
                sender.send(message)
                break
            } catch (ex: Exception) {
                logger.error("error when send email ", ex)
            }
        }
    }

    private val contentTemplate = """
            货物清单:
            %s

            地址:
            %s
            %s
            %s
            %s
        """.trimIndent()

    private fun newMessage(order: Order): SimpleMailMessage? {
        val message = SimpleMailMessage()
        message.from = "test-sender@example.com"
        message.to = arrayOf("test-reciever@example.com")
        message.subject = "订单 ${order.id} 已完成付款，等待出货"
        message.text = contentTemplate.format(
                materialList(order.bill) ?: return null,
                order.deliveryInfo.city,
                order.deliveryInfo.addressLine1,
                order.deliveryInfo.addressLine2,
                order.deliveryInfo.postalCode
        )
        return message
    }

    private val itemTemplate = "%d\t%s\t%d"
    private fun materialList(list: Iterable<ShoppingItem>): String? {
        return list.map {
            itemTemplate.format(
                    it.pid,
                    productService.productName(it.pid) ?: return null,
                    it.count)
        }.joinToString("\r\n")
    }
}
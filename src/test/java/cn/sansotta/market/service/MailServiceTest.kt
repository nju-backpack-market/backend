package cn.sansotta.market.service

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev_local")
class MailServiceTest {
    @Autowired
    lateinit var service: MailService

    @Test
    fun sendMail() {
        service.sendStockOutMail()
    }
}
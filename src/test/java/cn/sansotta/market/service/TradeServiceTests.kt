package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Trade
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

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev_test")
@Transactional
@SqlConfig(encoding = "UTF8")
@Sql("classpath:test_schema.sql", "classpath:trades_data.sql")
class TradeServiceTests : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    lateinit var service: TradeService

    @Test
    fun newTrade() {
        val trade1 = Trade(8L, "alipay", "0987654321", null)
        Thread.sleep(100)
        assertTrue(service.newTrade(trade1))

        val trade2 = service.getTradeLocked(8L)
        assertNotNull(trade2)
        assertEquals("0987654321", trade2!!.tradeId)
        assertEquals(5, countRowsInTable("trades"))
        assertFalse(service.newTrade(trade1))
    }

    @Test
    fun getTradeId() {
        assertEquals("1234567890", service.getTradeId(1))
        assertNull(service.getTradeId(3))
    }

    @Test
    fun getTradeByExtra() {
        assertEquals("asdfghjkl", service.getTradeByExtra("EC-0987654321")!!.tradeId)
        assertNull(service.getTradeByExtra("abc"))
    }

    @Test
    fun getTradeLocked() {
        assertEquals("1234567890", service.getTradeLocked(1)!!.tradeId)
        assertNull(service.getTradeLocked(3))
    }

    @Test
    fun voidTrade() {
        assertTrue(service.voidTrade(1))
        assertFalse(service.voidTrade(3))
        assertEquals(3, countRowsInTable("trades"))
    }

    @Test
    fun voidTradeExtra() {
        service.voidTradeExtra(1)
        assertNull(service.getTradeLocked(1)!!.extra)
    }
}
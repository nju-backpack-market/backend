package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
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
@ActiveProfiles("dev_local")
@Transactional
@SqlConfig(encoding = "UTF8")
@Sql("classpath:test_schema.sql", "classpath:products_data.sql")
class ProductServiceTests : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    lateinit var service: ProductService

    @Test
    fun singleProduct() {
        assertNull(service.product(-1, false))
        assertEquals(Product(1, "红包", 11.4, "Mock1", emptyList(), true), service.product(1, false))
    }

    @Test
    fun multiProducts() {
        assertEquals(4, service.allProducts(0, true)?.size)
        assertEquals(3, service.allProducts(0, false)?.size)
    }

    @Test
    fun findByName() {
        assertEquals(1, service.products("红", 0, true)?.size)
        assertEquals(4, service.products("包", 0, true)?.size)
        assertEquals(3, service.products("包", 0, false)?.size)
    }

    @Test
    fun createProducts() {
        val products = listOf(
                Product(5L, "蓝包", 23.33, "Mock5", listOf("1", "2"), false),
                Product(6L, "橙包", 46.66, "MOCK6", listOf("3", "4"), true),
                Product(-1L, "错了!", -1.0, "ERROR", emptyList(), false)) // this should not be inserted
        assertEquals(2, service.newProducts(products).size)
        assertEquals(6, countRowsInTable("products"))
        assertEquals(4, service.allProducts(0, false)!!.size)
        assertEquals(6, service.allProducts(0, true)!!.size)
        assertEquals(2, service.product(5L, true)!!.images.size)
        assertEquals(0, service.product(5L, false)!!.images.size)
    }

    @Test
    fun deleteProducts() {
        assertEquals(2, service.removeProducts(listOf(1, 2, 5)).size)
        assertEquals(4, countRowsInTable("products"))
        assertFalse(service.product(1, false)!!.onSale)
    }

    @Test
    fun updateProducts() {
        val products = listOf(
                Product(3, "", 0.01, "", emptyList(), true),
                Product(1, "哇哇哇", -1.0, "大减价！", emptyList(), true),
                Product(5, "NO", 10.0, "TEST", emptyList(), false))
        assertEquals(2, service.modifyProducts(products).size)
        assertEquals("绿包", service.product(3, false)?.name)
        assertEquals("大减价！", service.product(1, false)?.description)
        assertEquals(11.4, service.product(1, false)?.price)
        assertEquals(4, service.allProducts(0, false)!!.size)
    }
}

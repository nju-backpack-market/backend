package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
        assertEquals(4, service.allProducts(0)?.size)
        assertEquals(Product(1, "红包", 11.4, "Mock1", emptyList()), service.product(1, false))
    }

    @Test
    fun multiProducts() {
        assertEquals(4, service.allProducts(0)?.size)
    }

    @Test
    fun createProducts() {
        val products = listOf(
                Product(5, "蓝包", 23.33, "Mock5", listOf("1", "2")),
                Product(6, "橙包", 46.66, "MOCK6", listOf("3", "4")),
                Product(-1, "错了!", -1.0, "ERROR", emptyList())) // this should not be inserted
        assertEquals(2, service.newProducts(products).size)
        assertEquals(6, countRowsInTable("products"))
        println(service.allProducts(0))
    }

    @Test
    fun deleteProducts() {
        assertEquals(2, service.removeProducts(listOf(1, 2, 5)).size)
        assertEquals(2, countRowsInTable("products"))
    }

    @Test
    fun updateProducts() {
        val products = listOf(
                Product(3, "", 0.01, "", emptyList()),
                Product(1, "哇哇哇", -1.0, "大减价！", emptyList()),
                Product(5, "NO", 10.0, "TEST", emptyList()))
        assertEquals(2, service.modifyProducts(products).size)
        assertEquals("绿包", service.product(3, false)?.name)
        assertEquals("大减价！", service.product(1, false)?.description)
        assertEquals(11.4, service.product(1, false)?.price)
    }
}

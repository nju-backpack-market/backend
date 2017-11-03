package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Assert

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev_local")
@Transactional
@Sql("classpath:test_schema.sql", "classpath:products_data.sql")
class ProductServiceTests : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    lateinit var service: ProductService

    @Test
    fun singleProduct() {
        Assert.isNull(service.product(-1), "")
        Assert.state(service.allProducts(0)?.size == 4, "")
        Assert.state(service.product(1)?.equals(Product(1, "红包", 11.4, "Mock1")) ?: false, "")
    }

    @Test
    fun multiProducts() {
        Assert.state(service.allProducts(0)?.size == 4, "")
    }

    @Test
    fun createProducts() {
        val products = listOf(
                Product(5, "蓝包", 23.33, "Mock5"),
                Product(6, "橙包", 46.66, "MOCK6"),
                Product(-1, "错了!", -1.0, "ERROR")) // this should not be inserted
        assertEquals(service.newProducts(products)?.size, 2)
        assertEquals(countRowsInTable("products"), 6)
        println(service.allProducts(0))
    }

    @Test
    fun deleteProducts() {
        assertEquals(service.removeProducts(listOf(1, 2, 5))?.size, 2)
        assertEquals(countRowsInTable("products"), 2)
    }

    @Test
    fun updateProducts() {
        val products = listOf(
                Product(3, "", 0.01, ""),
                Product(1, "哇哇哇", -1.0, "大减价！"),
                Product(5, "NO", 10.0, "TEST"))
        assertEquals(service.modifyProducts(products)?.size, 2)
        assertEquals(service.product(3)?.name, "绿包")
        assertEquals(service.product(1)?.description, "大减价！")
        assertEquals(service.product(1)?.price, 11.4)
    }
}

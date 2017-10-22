package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.dao.ProductDao
import cn.sansotta.market.domain.entity.ProductEntity
import cn.sansotta.market.domain.value.Product
import cn.sansotta.market.service.ProductService
import com.github.pagehelper.PageInfo
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class ProductManager(private val productDao: ProductDao) : ProductService {
    private val mockProduct = ProductEntity(1, "MockProduct", 10000.0, "MOCK")
    private val mockProduct2 = ProductEntity(1, "MockProduct", 10000.0, "MOCK")
    override fun product(id: Long) = Product(mockProduct)

    override fun allProducts(page: Int)
            = copyPageInfo(PageInfo(listOf(mockProduct, mockProduct2))) { Product(it) }
}

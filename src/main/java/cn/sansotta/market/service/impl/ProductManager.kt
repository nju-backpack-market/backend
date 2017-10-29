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
    override fun newProducts(product: List<Product>): List<Product>? {
        product.mapIndexed { index, p -> p.pid = index.toLong();p }
        return product
    }

    override fun modifiedProduct(product: Product): Boolean {
        return true
    }

    override fun removeProducts(ids: List<Long>): Boolean {
        return true
    }

    private val mockProduct = ProductEntity(1, "MockProduct", 10000.0, "MOCK")
    private val mockProduct2 = ProductEntity(2, "MockProduct", 10005.0, "MOCK")
    override fun product(id: Long) = Product(mockProduct)

    override fun allProducts(page: Int)
            = copyPageInfo(PageInfo(listOf(mockProduct, mockProduct2))) { Product(it) }
}

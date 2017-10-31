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
    override fun newProducts(products: List<Product>): List<Product>? {
        // TODO: parameter should be valid entity
        return products.filter { Product.isValidEntity(it) }
                .mapIndexed { index, p -> p.pid = index.toLong();p }
    }

    override fun modifyProducts(products: List<Product>): List<Product>? {
        // TODO: pid should be valid
        return products.filter { it.pid > 0L }
    }

    override fun removeProducts(ids: List<Long>): List<Long>? {
        // TODO: id should be valid
        return ids.filter { it > 0L }
    }

    private val mockProduct = ProductEntity(1, "MockProduct", 10000.0, "MOCK")
    private val mockProduct2 = ProductEntity(2, "MockProduct", 10005.0, "MOCK")
    override fun product(id: Long) = Product(mockProduct)

    override fun allProducts(page: Int)
            = copyPageInfo(PageInfo(listOf(mockProduct, mockProduct2))) { Product(it) }
}

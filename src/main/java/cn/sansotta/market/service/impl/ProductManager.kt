package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.dao.ProductDao
import cn.sansotta.market.domain.value.Product
import cn.sansotta.market.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class ProductManager(private val productDao: ProductDao) : ProductService {
    private val logger = LoggerFactory.getLogger(ProductManager::class.java)

    //    private val mockProduct = ProductEntity(1, "MockProduct", 10000.0, "MOCK")
    //    private val mockProduct2 = ProductEntity(2, "MockProduct", 10005.0, "MOCK")
    override fun product(id: Long)
            = id.takeIf { it > 0L }
            ?.let(productDao::selectProductById)
            ?.let(::Product)

    override fun allProducts(page: Int)
            = page.takeIf { page >= 0 }
            ?.let(productDao::selectAllProducts)
            ?.let { copyPageInfo(it, ::Product) }

    override fun newProducts(products: List<Product>)
            = products
            .filter(Product.Companion::isValidEntity)
            .mapNotNull { obj ->
                hazard("new product") { productDao.insertProduct(obj.toEntity()) }
                        ?.let { entity -> obj.pid = entity.id;obj } // save memory...
            }

    @Transactional // to make sure the select and update is consistent
    override fun modifyProducts(products: List<Product>): List<Product> {
        val valid = products.filter { it.id > 0L }
        return valid.map { productDao.selectProductById(it.id) }
                .zip(valid)
                .filter { (origin, _) -> origin != null }
                .mapNotNull { (origin, modified) -> Product.mergeAsUpdate(origin, modified) }
                .filter { hazard("update product", false) { productDao.updateProduct(it.toEntity()) } }
    }

    override fun removeProducts(ids: List<Long>): List<Long> {
        return ids.filter { it > 0L && hazard("delete product", false) { productDao.deleteProduct(it) } }
    }

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                null
            }
}

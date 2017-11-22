package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.common.ifTrue
import cn.sansotta.market.dao.ProductDao
import cn.sansotta.market.domain.entity.ProductEntity
import cn.sansotta.market.domain.value.Product
import cn.sansotta.market.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class ProductManager(private val productDao: ProductDao) : ProductService {
    private val logger = LoggerFactory.getLogger(ProductManager::class.java)

    override fun product(id: Long, withPicture: Boolean)
            = id.takeIf { it > 0L }
            ?.let { productDao.selectProductById(id, withPicture) }
            ?.let(::Product)

    override fun allProducts(page: Int, all: Boolean)
            = page.takeIf { page >= 0 }
            ?.let { productDao.selectAllProducts(it, !all) }
            ?.let { copyPageInfo(it, ::Product) }

    override fun products(name: String, page: Int, all: Boolean)
            = page.takeIf { page >= 0 }
            ?.let { productDao.selectProductByName(name, page, !all) }
            ?.let { copyPageInfo(it, ::Product) }

    override fun productName(id: Long) = id.takeIf { id > 0L }?.let(productDao::selectProductName)

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
        return valid.map { productDao.selectProductById(it.id, true) }
                .zip(valid)
                .mapNotNull { (origin, modified) -> origin?.let { doModifyProduct(it, modified) } }
    }

    private fun doModifyProduct(old: ProductEntity, new: Product): Product? {
        val merged = Product.mergeAsUpdate(old, new) ?: return null
        val deletedImages = old.images - merged.images
        return merged.takeIf { p ->
            hazard("update product", false) {
                deletedImages.all { productDao.deleteProductImage(p.pid, it) } &&
                        productDao.updateProduct(p.toEntity())
            }.ifTrue { FileManager.deleteImage(deletedImages) }
        }
    }

    override fun removeProducts(ids: List<Long>): List<Long> {
        return ids.filter { it > 0L && hazard("delete product", false) { productDao.deleteProduct(it) } }
    }

    override fun pullOffProducts(ids: List<Long>)
            = ids.filter { it > 0 }
            .mapNotNull { product(it, false)?.takeIf(Product::onSale)?.apply { onSale = false } }
            .let(this::modifyProducts)
            .map(Product::pid)

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                null
            }
}

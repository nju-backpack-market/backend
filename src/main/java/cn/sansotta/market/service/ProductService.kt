package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface ProductService {
    fun allProducts(page: Int, all: Boolean): PageInfo<Product>?
    fun product(id: Long, withPicture: Boolean): Product?
    fun products(name: String, page: Int, all: Boolean): PageInfo<Product>?
    fun productName(id: Long): String?
    fun newProducts(products: List<Product>): List<Product>
    fun modifyProducts(products: List<Product>): List<Product>
    fun removeProducts(ids: List<Long>): List<Long>
    fun pullOffProducts(ids: List<Long>): List<Long>
}
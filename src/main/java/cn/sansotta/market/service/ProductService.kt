package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface ProductService {
    fun allProducts(page: Int): PageInfo<Product>?
    fun product(id: Long): Product?
    fun newProducts(product: List<Product>): List<Product>?
    fun modifiedProduct(product: Product): Boolean
    fun removeProducts(ids: List<Long>): Boolean
}
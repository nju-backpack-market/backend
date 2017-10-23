package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Product
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface ProductService {
    fun allProducts(page: Int): PageInfo<Product>?
    fun product(id: Long): Product?
}
package cn.sansotta.market.controller.resource

import cn.sansotta.market.domain.value.Product
import org.springframework.hateoas.Resource

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ProductResource(product: Product) : Resource<Product>(product) {
    val id = product.id
    val name = product.name
    val price = product.price
    val description = product.description
}
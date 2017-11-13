package cn.sansotta.market.controller.resource

import cn.sansotta.market.domain.value.Product
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.hateoas.Resource

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonIgnoreProperties("content")
class ProductResource(product: Product) : Resource<Product>(product) {
    val pid = product.pid
    val name = product.name
    val price = product.price
    val description = product.description
    val onSale = product.onSale
}
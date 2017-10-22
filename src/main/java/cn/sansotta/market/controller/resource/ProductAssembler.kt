package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.ProductsController
import cn.sansotta.market.domain.value.Product
import org.springframework.hateoas.mvc.ResourceAssemblerSupport

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ProductAssembler
    : ResourceAssemblerSupport<Product, ProductResource>(ProductsController::class.java,
        ProductResource::class.java) {
    override fun toResource(product: Product) = createResourceWithId(product.id, product)

    override fun instantiateResource(entity: Product) = ProductResource(entity)
}
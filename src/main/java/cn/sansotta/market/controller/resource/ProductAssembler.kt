package cn.sansotta.market.controller.resource

import cn.sansotta.market.common.linkTo
import cn.sansotta.market.controller.ProductController
import cn.sansotta.market.domain.value.Product
import org.springframework.hateoas.mvc.ResourceAssemblerSupport

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ProductAssembler
    : ResourceAssemblerSupport<Product, ProductResource>(ProductController::class.java,
        ProductResource::class.java) {
    override fun toResource(product: Product) =
            createResourceWithId(product.id, product).apply {
                add(linkTo<ProductController>().withRel("collection"))
            }

    override fun instantiateResource(entity: Product) = ProductResource(entity)
}
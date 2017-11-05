package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.ProductsController
import cn.sansotta.market.domain.value.ShoppingItem
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ShoppingItemResource(shoppingItem: ShoppingItem) : Resource<ShoppingItem>(shoppingItem) {
    init {
        add(linkTo(methodOn(ProductsController::class.java).product(shoppingItem.pid))
                .withRel("get_product"))
    }
}

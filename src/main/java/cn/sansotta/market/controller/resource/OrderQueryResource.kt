package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.OrdersController
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class OrderQueryResource(orderQuery: OrderQuery) : Resource<OrderQuery>(orderQuery) {
    init {
        add(linkTo(
                methodOn(OrdersController::class.java).query(orderQuery.queryId, 0, true)).withSelfRel())
    }
}
package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.OrdersController
import cn.sansotta.market.domain.value.Order
import org.springframework.hateoas.mvc.ResourceAssemblerSupport

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class OrderAssembler : ResourceAssemblerSupport<Order, OrderResource>(OrdersController::class.java,
        OrderResource::class.java) {
    override fun toResource(entity: Order) = createResourceWithId(entity.getId(), entity)

    override fun instantiateResource(entity: Order) = OrderResource(entity)

}
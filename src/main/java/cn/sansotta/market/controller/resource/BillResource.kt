package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.OrdersController
import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.ShoppingItem
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class BillResource(bill: Bill, withLink: Boolean = true) : Resource<Bill>(bill) {
    init {
        if (withLink)
            add(linkTo(methodOn(OrdersController::class.java).createOrder(Order())).
                    withRel("create_order"))
    }
}

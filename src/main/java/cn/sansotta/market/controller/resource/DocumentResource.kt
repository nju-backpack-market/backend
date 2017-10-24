package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.BillsController
import cn.sansotta.market.controller.OrdersController
import cn.sansotta.market.controller.PingController
import cn.sansotta.market.controller.ProductsController
import cn.sansotta.market.domain.value.Order
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpMethod

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
open class DocumentResource internal constructor() : Resource<String>("Document") {
    init {
        apis<PingController> {
            api("self", HttpMethod.GET) { api() }
            api("get_api_info", HttpMethod.GET) { apiInfo() }
        }
        apis<ProductsController> {
            api("get_all_products", HttpMethod.GET) { allProducts(0) }
            api("get_product", HttpMethod.GET) { product(1) }
        }
        apis<BillsController> {
            api("query_price", HttpMethod.POST) { createBill(listOf()) }
        }
        apis<OrdersController> {
            api("create_order", HttpMethod.POST) { createOrder(Order()) }
        }
    }

    private inline fun <reified T> apis(apiInfo: Class<T>.() -> Unit) = T::class.java.apiInfo()

    private fun <T> Class<T>.api(apiName: String, method: HttpMethod, example: T.() -> Any) {
        add(linkTo(methodOn(this).example()).withRel(apiName))
        CustomizedCurieProvider.registerMethod(apiName, method)
    }
}

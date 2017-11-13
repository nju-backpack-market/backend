package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.*
import cn.sansotta.market.domain.value.Order
import cn.sansotta.market.domain.value.OrderStatus
import cn.sansotta.market.domain.value.Product
import cn.sansotta.market.domain.value.User
import org.springframework.hateoas.Link
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpMethod
import java.util.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
open class DocumentResource private constructor() : Resource<String>("Document") {
    companion object {
        @JvmStatic
        val INSTANCE by lazy { DocumentResource() }
    }

    init {
        apis<PingController> { api("get_api_info", HttpMethod.GET) { apiInfo() } }
        apis<ProductsController> {
            api("get_all_products", HttpMethod.GET) { allProducts(0, false, false) }
            api("get_products_by_name", HttpMethod.GET) { products("包", 0, false, false) }
            api("get_product", HttpMethod.GET) { product(1) }
            api("create_products", HttpMethod.POST) {
                newProducts(Collections.singletonList(Product.mockObject()))
            }
            api("update_products", HttpMethod.PUT) {
                modifyProducts(Collections.singletonList(Product.mockObject()))
            }
            api("delete_products", HttpMethod.DELETE) { removeProducts(listOf(1, 2)) }
        }
        apis<BillsController> {
            api("query_price", HttpMethod.POST) { createBill(listOf()) }
            api("get_bill_of_order", HttpMethod.GET) { billOfOrder(1) }
        }
        apis<OrdersController> {
            api("create_order", HttpMethod.POST) { createOrder(Order.mockObject()) }
            api("get_all_orders", HttpMethod.GET) { orders(0, true) }
            api("get_order", HttpMethod.GET) { order(1) }
            api("update_order_status", HttpMethod.PATCH) { modifyOrderStatus(1L, OrderStatus.CREATE) }
            api("update_order_info", HttpMethod.PUT) { modifyOrder(listOf(Order())) }
            api("create_query", HttpMethod.POST) { createQuery(OrderQuery().apply { id = 1 }, true) }
            api("query_order", HttpMethod.GET) { query(OrderQuery().apply { id = 1 }.queryId, 0) }
            api("start_payment",HttpMethod.POST,"/orders/{id}/payment?method=alipay")
        }
        apis<FilesController> {
            api("upload_image", HttpMethod.POST, "/files/image")
            api("upload_video", HttpMethod.POST, "/files/image")
        }
        apis<TokensController> {
            api("login", HttpMethod.POST) { createToken("111", "111") }
            api("logout", HttpMethod.DELETE) { deleteToken("111") }
        }
        apis<UsersController> {
            api("create_user", HttpMethod.POST) { createUser(User("111", "111")) }
            api("delete_user", HttpMethod.DELETE) { deleteUser("111") }
            api("modify_password", HttpMethod.PATCH) { modifyPassword("111", "222") }
        }
    }

    private inline fun <reified T> apis(apiInfo: Class<T>.() -> Unit) = T::class.java.apiInfo()

    private fun <T> Class<T>.api(apiName: String, method: HttpMethod, example: T.() -> Any) {
        add(linkTo(methodOn(this).example()).withRel(apiName))
        CustomizedCurieProvider.registerMethod(apiName, method)
    }

    private fun api(apiName: String, method: HttpMethod, uri: String) {
        add(Link(uri, apiName))
        CustomizedCurieProvider.registerMethod(apiName, method)
    }
}

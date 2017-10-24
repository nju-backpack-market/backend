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
open class ApiInfoResource internal constructor() : Resource<ApiInfo>(apiInfo) {
    init {
        apis<PingController> { api("self", HttpMethod.GET) { api() } }
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

data class ApiInfo(
        val version: String,
        val description: String,
        val convention: Array<String>)

val apiInfo = ApiInfo(
        "v0.0.1",
        "后端的API列表，附带例子和文档，点击左边的链接查看",
        arrayOf("endpoint形如/{res_type}, res_type均为复数形式。直接GET返回所有资源，/{res_type}/[id]返回单个资源",
                "返回所有资源时默认分页，页大小20，用?page=[int]来指示页数，缺省则page=0",
                "GET方法没有文档，直接看例子。 POST请查看文档并复制其中的例子，用例子进行NON-GET请求并查看返回值",
                "POST成功默认状态值201，DELETE成功默认状态值204，未找到资源状态值404，无授权访问受限API状态值403").
                mapIndexed { index, s -> "${index + 1}. $s" }.toTypedArray()
)

val apiInfoResource = ApiInfoResource()


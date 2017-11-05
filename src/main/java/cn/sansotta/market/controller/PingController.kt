package cn.sansotta.market.controller

import cn.sansotta.market.common.HAL_MIME_TYPE
import cn.sansotta.market.controller.resource.DocumentResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.hateoas.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 *
 * An endpoint just for test server's availability.
 */
@RestController
class PingController {
    @Autowired
    lateinit var m:CacheManager

    @GetMapping("/ping")
    fun ping() = "Hello"

    @GetMapping("/api")
    fun redirect(response: HttpServletResponse)
            = response.sendRedirect("/api/v1/browser/index.html#/api")

    @GetMapping("/api", produces = arrayOf(HAL_MIME_TYPE))
    fun api() = DocumentResource.INSTANCE

    @GetMapping("/apiInfo", produces = arrayOf(HAL_MIME_TYPE))
    fun apiInfo() = apiInfoResource

    open class ApiInfo(
            val version: String,
            val description: String,
            val convention: Array<String>)

    private val apiInfoResource = Resource(
            ApiInfo("v0.0.1",
                    "后端的API列表，附带例子和文档，点击左边的链接查看",
                    arrayOf("endpoint形如/{res_type}, res_type均为复数形式",
                            "直接GET返回所有资源，/{res_type}/[id]返回单个资源",
                            "返回所有资源时默认分页，页大小20，用?page=[int]来指示页数，缺省则page=0",
                            "GET和DELETE方法没有文档，直接看例子。" +
                                    "POST和PUT请查看文档并复制其中的例子，用例子进行NON-GET请求并查看返回值",
                            "POST成功默认状态值201，DELETE成功默认状态值204，未找到资源状态值404，无授权访问受限API状态值403, " +
                                    "数据库错误507（此错误为暂时，应尝试重新请求），内部错误500",
                            "大部分批量修改性api会返回修改成功了的实体列表。如PUT返回修改后的实体，DELETE返回成功删除了的id").
                            mapIndexed { index, s -> "${index + 1}. $s" }.toTypedArray()))
}
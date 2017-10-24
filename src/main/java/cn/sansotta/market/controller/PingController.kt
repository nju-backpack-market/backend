package cn.sansotta.market.controller

import cn.sansotta.market.common.HAL_MIME_TYPE
import cn.sansotta.market.controller.resource.DocumentResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 *
 * An endpoint just for test server's availability.
 */
@RestController
class PingController(@Autowired val db: DataSource) {
    @GetMapping("/ping")
    fun ping() = "Hello"

    @GetMapping("/conn")
    fun conn() = "${db.connection}"

    @GetMapping("/api")
    fun redirect(response: HttpServletResponse)
            = response.sendRedirect("/api/v1/browser/index.html#/api")

    @GetMapping("/api", produces = arrayOf(HAL_MIME_TYPE))
    fun api() = DocumentResource()

    @GetMapping("/apiInfo", produces = arrayOf(HAL_MIME_TYPE))
    fun apiInfo() = apiInfoResource

    class ApiInfo(
            val version: String,
            val description: String,
            val convention: Array<String>)

    private val apiInfoResource = Resource(
            ApiInfo("v0.0.1",
                    "后端的API列表，附带例子和文档，点击左边的链接查看",
                    arrayOf("endpoint形如/{res_type}, res_type均为复数形式",
                            "直接GET返回所有资源，/{res_type}/[id]返回单个资源",
                            "返回所有资源时默认分页，页大小20，用?page=[int]来指示页数，缺省则page=0",
                            "GET方法没有文档，直接看例子。 POST请查看文档并复制其中的例子，用例子进行NON-GET请求并查看返回值",
                            "POST成功默认状态值201，DELETE成功默认状态值204，未找到资源状态值404，无授权访问受限API状态值403").
                            mapIndexed { index, s -> "${index + 1}. $s" }.toTypedArray()))
}
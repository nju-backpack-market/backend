package cn.sansotta.market.controller

import cn.sansotta.market.common.HAL_MIME_TYPE
import cn.sansotta.market.controller.resource.ApiInfoResource
import org.springframework.beans.factory.annotation.Autowired
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
    fun api() = ApiInfoResource()
}
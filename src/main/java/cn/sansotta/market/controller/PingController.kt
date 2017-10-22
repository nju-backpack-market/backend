package cn.sansotta.market.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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

    @GetMapping("/test")
    fun test(@RequestParam("a") a:List<String>) = a
}
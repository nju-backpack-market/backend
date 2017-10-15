package cn.sansotta.market.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 *
 * An endpoint just for test server's availability.
 */
@RestController
class PingController {
    @GetMapping("/ping")
    fun ping() = "Hello!"
}
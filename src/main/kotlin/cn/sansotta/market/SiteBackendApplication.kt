package cn.sansotta.market

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SiteBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(SiteBackendApplication::class.java, *args)
}

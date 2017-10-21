package cn.sansotta.market.controller

import cn.sansotta.market.common.methodOn
import cn.sansotta.market.common.toResponse
import cn.sansotta.market.domain.Bill
import cn.sansotta.market.service.BusinessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/business")
class BusinessController(@Autowired private val businessService: BusinessService) {
}
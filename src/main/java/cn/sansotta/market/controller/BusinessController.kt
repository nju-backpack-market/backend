package cn.sansotta.market.controller

import cn.sansotta.market.domain.ShoppingList
import cn.sansotta.market.service.BusinessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/business")
class BusinessController(@Autowired private val businessService: BusinessService) {
    @GetMapping("/price")
    fun getPrice(@RequestParam("shopping_list") shoppingList: ShoppingList)
            = businessService.getTotalPrice(shoppingList)
}
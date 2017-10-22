package cn.sansotta.market.controller

import cn.sansotta.market.common.toResponse
import cn.sansotta.market.domain.value.Bill
import cn.sansotta.market.service.BusinessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/bill")
@ExposesResourceFor(Bill::class)
class BillController(@Autowired val link: EntityLinks,
                     @Autowired val businessService: BusinessService) {
    private var bill: Bill? = null

    @GetMapping(produces = arrayOf("application/hal+json"))
    fun createBill(@RequestBody shoppingList: Bill): ResponseEntity<Bill?>
            = businessService.getTotalPrice(shoppingList).apply {
        actualTotalPrice = originTotalPrice - 3
        bill = this
    }.toResponse()
}
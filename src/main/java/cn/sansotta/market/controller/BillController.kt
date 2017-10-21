package cn.sansotta.market.controller

import cn.sansotta.market.common.toResponse
import cn.sansotta.market.controller.resource.BillAssembler
import cn.sansotta.market.controller.resource.BillResource
import cn.sansotta.market.domain.Bill
import cn.sansotta.market.service.BusinessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/bill")
@ExposesResourceFor(Bill::class)
class BillController(@Autowired val link: EntityLinks,
                     @Autowired val businessService: BusinessService) {
    private var bill: Bill? = null

    @PostMapping(consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun createBill(@RequestBody shoppingList: Bill): ResponseEntity<Bill?>
            = businessService.getTotalPrice(shoppingList).apply {
        actualTotalPrice = originTotalPrice - 3
        billDiscountReason = "测试中文Json化"
        bill = this
    }.toResponse()

    @GetMapping("/{id}")
    fun getBillOf(@PathVariable("id") oid: Int): ResponseEntity<BillResource?>
            = bill?.let { BillAssembler().toResource(it) }.toResponse()
}
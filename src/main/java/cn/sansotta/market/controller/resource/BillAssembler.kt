package cn.sansotta.market.controller.resource

import cn.sansotta.market.controller.BillController
import cn.sansotta.market.domain.Bill
import org.springframework.hateoas.mvc.ResourceAssemblerSupport

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class BillAssembler :
        ResourceAssemblerSupport<Bill, BillResource>(BillController::class.java,
                BillResource::class.java) {
    override fun toResource(bill: Bill) = createResourceWithId(bill.id, bill)
}
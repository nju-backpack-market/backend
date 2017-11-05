package cn.sansotta.market.controller;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.controller.resource.BillResource;
import cn.sansotta.market.domain.value.Bill;
import cn.sansotta.market.domain.value.ShoppingItem;
import cn.sansotta.market.service.BillService;

import static cn.sansotta.market.common.HateoasUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.notFoundResponse;
import static cn.sansotta.market.common.HateoasUtils.toResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Bill.class)
@RequestMapping("/bills")
public class BillsController {
    private final BillService billService;

    public BillsController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<BillResource>
    createBill(@RequestBody List<ShoppingItem> items) {
        Bill bill = billService.queryPrice(items);
        return bill == null ? notFoundResponse() : toResponse(new BillResource(bill, true));
    }

    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<BillResource>
    billOfOrder(@RequestParam("oid") Long oid) {
        Bill bill = billService.billOfOrder(oid);
        return bill == null ? notFoundResponse() : toResponse(new BillResource(bill, true));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public void head(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }
}

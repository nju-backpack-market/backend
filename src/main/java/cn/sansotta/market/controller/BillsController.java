package cn.sansotta.market.controller;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.controller.resource.BillResource;
import cn.sansotta.market.domain.value.Bill;
import cn.sansotta.market.domain.value.ShoppingItem;
import cn.sansotta.market.service.BillService;

import static cn.sansotta.market.common.HateoasUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.toResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Bill.class)
@RequestMapping("/bills")
public class BillsController {
    private final EntityLinks link;
    private final BillService billService;

    public BillsController(BillService billService, EntityLinks link) {
        this.billService = billService;
        this.link = link;
    }

    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<BillResource> createBill(@RequestBody List<ShoppingItem> items) {
        return toResponse(new BillResource(billService.queryPrice(items)));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public void head(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }
}

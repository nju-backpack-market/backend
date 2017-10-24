package cn.sansotta.market.controller;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sansotta.market.domain.value.Order;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Order.class)
@RequestMapping("/orders")
public class OrdersController {
    @PostMapping
    public ResponseEntity createOrder(Order order) {
        return new ResponseEntity(HttpStatus.OK);
    }
}

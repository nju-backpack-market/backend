package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.controller.resource.OrderAssembler;
import cn.sansotta.market.controller.resource.OrderResource;
import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.service.OrderService;

import static cn.sansotta.market.common.HateoasUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.notFoundEntity;
import static cn.sansotta.market.common.HateoasUtils.pagedResourcesBatch;
import static cn.sansotta.market.common.HateoasUtils.toResponse;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Order.class)
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;
    private final EntityLinks link;
    private final OrderAssembler assembler = new OrderAssembler();

    public OrdersController(OrderService orderService, EntityLinks link) {
        this.orderService = orderService;
        this.link = link;
    }

    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<OrderResource> createOrder(@RequestBody Order order) {
        Order responseOrder = orderService.createOrder(order);
        if(responseOrder == null)
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        else if(responseOrder.getId() == -1)
            return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
        else {
            HttpHeaders header = new HttpHeaders();
            header.add("Location", "/orders/" + responseOrder.getId());
            return new ResponseEntity<>(assembleResource(responseOrder), header, HttpStatus.CREATED);
        }
    }

    @GetMapping(value = "/{id}", produces = HAL_MIME_TYPE)
    public ResponseEntity<OrderResource> order(@PathVariable("id") long id) {
        Order order = orderService.order(id);
        return order == null ? notFoundEntity() : toResponse(new OrderResource(order));
    }

    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<OrderResource>>
    orders(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
           @RequestParam(value = "full", required = false, defaultValue = "false") boolean full) {
        PageInfo<Order> pageInfo;
        if(full) pageInfo = orderService.allOrders(page);
        else pageInfo = orderService.allOrdersIndex(page);
        return pageInfo == null ? notFoundEntity() : toResponse(assembleResources(pageInfo));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public void head(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }

    private OrderResource assembleResource(Order order) {
        OrderResource resource = assembler.toResource(order);
        resource.add(link.linkToSingleResource(order));
        resource.add(link.linkToCollectionResource(Order.class).withRel("collection"));
        return resource;
    }

    private PagedResources<OrderResource> assembleResources(PageInfo<Order> info) {
        PagedResources<OrderResource> resources = pagedResourcesBatch(info, assembler::toResources);
        resources.add(
                linkTo(methodOn(getClass()).orders(info.getPageNum(), false)).withSelfRel(),
                linkTo(methodOn(getClass()).orders(info.getNavigateFirstPage(), false)).withRel("first"),
                linkTo(methodOn(getClass()).orders(info.getNavigateLastPage(), false)).withRel("last")
        );
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).orders(info.getPrePage(), false)).withRel("prev"));
        if(info.isHasNextPage())
            resources
                    .add(linkTo(methodOn(getClass()).orders(info.getNextPage(), false)).withRel("next"));
        return resources;
    }
}

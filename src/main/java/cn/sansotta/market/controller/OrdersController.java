package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.controller.resource.OrderAssembler;
import cn.sansotta.market.controller.resource.OrderQuery;
import cn.sansotta.market.controller.resource.OrderQueryResource;
import cn.sansotta.market.controller.resource.OrderResource;
import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.domain.value.OrderStatus;
import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.OrderService;
import cn.sansotta.market.service.PaymentService;

import static cn.sansotta.market.common.HateoasUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.badRequestResponse;
import static cn.sansotta.market.common.HateoasUtils.conflictResponse;
import static cn.sansotta.market.common.HateoasUtils.insufficientStorageResponse;
import static cn.sansotta.market.common.HateoasUtils.notFoundResponse;
import static cn.sansotta.market.common.HateoasUtils.pagedResourcesBatch;
import static cn.sansotta.market.common.HateoasUtils.toResponse;
import static cn.sansotta.market.common.HateoasUtils.unauthorizedResponse;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Order.class)
@RequestMapping("/orders")
public class OrdersController {
    private final EntityLinks link;
    private final OrderAssembler assembler = new OrderAssembler();
    private final PaymentService payPalService;
    private final OrderService orderService;

    private static final String PAYPAL = "paypal";
    private static final String ALIPAY = "alipay";

    public OrdersController(OrderService orderService,
                            EntityLinks link,
                            @Qualifier("payPalManager") PaymentService payPalService) {
        this.link = link;
        this.orderService = orderService;
        this.payPalService = payPalService;
    }

    @GetMapping(value = "/{id}", produces = HAL_MIME_TYPE)
    public ResponseEntity<OrderResource>
    order(@PathVariable("id") long id) {
        Order order = orderService.order(id);
        return order == null ? notFoundResponse() : toResponse(assembleResource(order));
    }

    @Authorized
    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<OrderResource>>
    orders(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
           @RequestParam(value = "full", required = false, defaultValue = "false") boolean full) {
        PageInfo<Order> pageInfo;
        if(full) pageInfo = orderService.allOrders(page);
        else pageInfo = orderService.allOrdersIndex(page);
        return pageInfo == null ? notFoundResponse() : toResponse(assembleResources(pageInfo));
    }


    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<OrderResource>
    createOrder(@RequestBody Order order) {
        Order responseOrder = orderService.newOrder(order);
        if(responseOrder == null)
            return badRequestResponse();
        else if(responseOrder.getId() == -1)
            return insufficientStorageResponse();
        else if(responseOrder.getId() == -2)
            return conflictResponse();
        else {
            HttpHeaders header = new HttpHeaders();
            header.add("Location", "/orders/" + responseOrder.getId());
            return new ResponseEntity<>(assembleResource(responseOrder), header, HttpStatus.CREATED);
        }
    }

    @Authorized(intercept = false)
    @PostMapping(value = "/query", produces = HAL_MIME_TYPE)
    public ResponseEntity<OrderQueryResource>
    createQuery(@RequestBody OrderQuery query,
                @RequestAttribute("authorized") boolean authorized) {
        query = orderService.createOrderQuery(query, authorized);
        return query == null ? badRequestResponse() :
                query.getQueryId() == -1 ? unauthorizedResponse() :
                        toResponse(new OrderQueryResource(query), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/query", method = RequestMethod.HEAD)
    public void createQueryHead(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }

    @Authorized(intercept = false)
    @GetMapping(value = "/query/{id}", produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<OrderResource>>
    query(@PathVariable("id") int queryId,
          @RequestParam(value = "page", required = false, defaultValue = "0") int page,
          @RequestParam(value = "full", required = false, defaultValue = "true") boolean full) {
        OrderQuery query = orderService.getOrderQuery(queryId);
        if(query == null) return badRequestResponse();

        PageInfo<Order> pageInfo = orderService.queryOrders(page, query);
        return pageInfo == null ? insufficientStorageResponse() :
                pageInfo.getSize() == 0 ? notFoundResponse() :
                        toResponse(assembleResources(pageInfo));
    }

    @Authorized(intercept = false)
    @PutMapping(value = "/{id}/status", produces = HAL_MIME_TYPE)
    public ResponseEntity
    modifyOrderStatus(@PathVariable("id") Long id, @RequestParam("modified") OrderStatus status) {
        Order updated = orderService.modifyOrderStatus(id, status);
        if(updated == null) return badRequestResponse();
        else if(updated.getId() == -1) return insufficientStorageResponse();
        else return toResponse(assembleResource(updated));
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.HEAD)
    public void modifyOrderStatusHead(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }

    @Authorized
    @PutMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity
    modifyOrder(@RequestBody List<Order> orders) {
        return toResponse(assembleResources(orderService.modifyOrders(orders)));
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public void modifyOrderHead(HttpServletResponse response) {
        response.setHeader("Content-Type", HAL_MIME_TYPE);
    }

    @PostMapping("/{orderId}/payment")
    public void
    createPayment(@PathVariable("orderId") long orderId,
                  @RequestParam("method") String method,
                  HttpServletResponse response) throws IOException {
        if(!PAYPAL.equals(method) && !ALIPAY.equals(method)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Order order = orderService.paymentBegin(orderId); // now order's status change to PAYING
        if(order == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (method) {
            case PAYPAL:
                payPalPayment(order, response);
                break;
        }
    }

    private void payPalPayment(Order order, HttpServletResponse response) throws IOException {
        Payment payment = payPalService.createPayment(order);
        if(payment == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        for (Links links : payment.getLinks())
            if("approval_url".equals(links.getRel()))
                response.sendRedirect(links.getHref());
    }

    private OrderResource assembleResource(Order order) {
        OrderResource resource = assembler.toResource(order);
        resource.add(link.linkToCollectionResource(Order.class).withRel("collection"));
        return resource;
    }

    private List<OrderResource> assembleResources(List<Order> orders) {
        return assembler.toResources(orders);
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

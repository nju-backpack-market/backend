package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.controller.resource.OrderAssembler;
import cn.sansotta.market.controller.resource.OrderQuery;
import cn.sansotta.market.controller.resource.OrderQueryResource;
import cn.sansotta.market.controller.resource.OrderResource;
import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.domain.value.OrderStatus;
import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.CommonPaymentService;
import cn.sansotta.market.service.OrderService;

import static cn.sansotta.market.common.WebUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.badRequestResponse;
import static cn.sansotta.market.common.WebUtils.conflictResponse;
import static cn.sansotta.market.common.WebUtils.insufficientStorageResponse;
import static cn.sansotta.market.common.WebUtils.notFoundResponse;
import static cn.sansotta.market.common.WebUtils.pagedResourcesBatch;
import static cn.sansotta.market.common.WebUtils.toResponse;
import static cn.sansotta.market.common.WebUtils.unauthorizedResponse;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@RestController
@ExposesResourceFor(Order.class)
@RequestMapping("/orders")
public class OrdersController {
    private final EntityLinks link;
    private final OrderAssembler assembler = new OrderAssembler();
    private final CommonPaymentService paymentService;
    private final OrderService orderService;

    private static final String PAYPAL = "paypal";
    private static final String ALIPAY = "alipay";

    public OrdersController(OrderService orderService,
                            EntityLinks link,
                            CommonPaymentService paymentService) {
        this.link = link;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Authorized
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
        PageInfo<Order> pageInfo = orderService.allOrders(page, full);
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
          @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        OrderQuery query = orderService.getOrderQuery(queryId);
        if(query == null) return badRequestResponse();

        PageInfo<Order> pageInfo = orderService.queryOrders(page, query);
        return pageInfo == null ? insufficientStorageResponse() :
                pageInfo.getSize() == 0 ? notFoundResponse() :
                        toResponse(assembleResources(pageInfo));
    }

    @Authorized(intercept = false)
    @PatchMapping(value = "/{id}", produces = HAL_MIME_TYPE)
    public ResponseEntity
    modifyOrderStatus(@PathVariable("id") Long id, @RequestParam("status") OrderStatus status) {
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

    @DeleteMapping("/{orderId}/payment")
    public ResponseEntity<OrderResource>
    voidPayment(@PathVariable("orderId") long orderId) {
        Order order = paymentService.paymentCancel(orderId);
        if(order == null) return badRequestResponse();
        else return toResponse(assembleResource(order));
    }

    /**
     * 为维护着想，记录下交易的控制方式。
     *
     * 交易从createPayment开始，通过paymentBegin改变order的状态。paymentBegin是同步的（通过select for update)，
     * 如果order的当前状态是CREATE，则有且仅会有一次修改成功。至此createPayment本身进入并发安全状态。但是允许voidPayment
     * 并发地被调用。
     *
     * 之后会进行到CommonPaymentManager.startPayPalPayment或startAlipayPayment，这两个方法中会对order同步。如果
     * 之前void正在执行，start会等待其完成。或者是反过来，直到start的流程结束后void才会被继续。所以start和void的过程中
     * 总是同步的。
     *
     * start的过程中如果发生错误，会调用paymentCancel.
     *
     * paymentCancel涉及Order状态的更新，Trade的更新和支付平台的同步。所以在一开始就用for update锁住订单。PayPal不需要
     * 同步。支付宝的同步通过closeTrade接口，如果这里有异常，会通过queryTrade检查状态，如果其实已经不在wait_buyer_pay状态
     * 则其实cancel已完成。如果还是则抛出异常。其他的异常会抛出，导致回滚，防止不一致。
     *
     * start完成后，sdk产生跳转的目标。返回给客户端，至此createPayment结束。
     *
     * PayPal:
     * PayPal的跳转并不是真正的付款，而是用户批准。之后PayPal会通过/payments/paypal/return回调，此时调用sdk的execute
     * 执行获得获得批准的订单。这个api会充当一个同步措施，即使我们发了多次请求有只会有一次成功，所以不用我们来同步，直接请求即可。
     * 如果出错了则说明是不合法的。然后会tryDone。注意在createPayment完成后到收到回调前可能会有并发的voidPayment，tryDone
     * 会等待void完成，然后在paymentDone处失败返回null，不会导致不一致。而tryDone本身会锁住order，tryDone过程中不会order
     * 状态不会被修改，所以一定会成功。之后再有voidPayment则都会失败了。
     *
     * 而用户如果取消订单则会通知/payments/paypal/cancel，反正是取消，和voidPayment的处理是一样的。
     *
     * 支付宝：
     * 支付宝跳转就是真正的去付款了，回调时则是付款完成了。由于验签所以不会有伪造的消息。收到通知并检验通过则通过
     * AlipayPaymentService.handleSuccessNotification来处理。这个函数中一开始就会锁住order。里面的那些错误处置是
     * 为了防止回调消息在网络中延误，导致接收到重复的消息的情况。或者用户手贱开另外一个窗口voidPayment了，这里的paymentDone
     * 自然不会成功。并且付款完成前数据库里没有trade的记录自然也没法调用closeTrade关闭支付宝那边的交易。不管，超时时间设为
     * 了3h，等超时再付款吧，要么去支付宝账单里关闭。反正我不提供任何关闭的接口。如果任何一步失败了返回"fail"即可。如果收到了
     * close则调用cancel，和voidPayment也是一样的。
     *
     * 一切ok之后，voidPayment就总会是失败的了
     * */
    @PostMapping("/{orderId}/payment")
    public void
    createPayment(@PathVariable("orderId") long orderId,
                  @RequestParam("method") String method,
                  HttpServletResponse response) throws IOException {
        if(!PAYPAL.equals(method) && !ALIPAY.equals(method)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Order order = paymentService.paymentBegin(orderId); // now order's status change to PAYING
        // we don't care about the actual status of trade in PayPal or Alipay. Payment commencement
        // fails if only the status in our database is not CREATE. User should explicitly void previous
        // payment if they want to restart the trade.
        if(order == null || order.getStatus() != OrderStatus.PAYING) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        // paymentBegin is concurrency-safe, only the first call will succeed.
        // so always only one thread for one order will continue from here.
        switch (method) {
            case PAYPAL:
                payPalPayment(order, response);
                break;
            case ALIPAY:
                alipayPayment(order, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void payPalPayment(Order order, HttpServletResponse response) throws IOException {
        String redirect = paymentService.startPayPalPayment(order);

        if(redirect == null) response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else response.sendRedirect(redirect);
    }

    private void alipayPayment(Order order, HttpServletResponse response) throws IOException {
        final String form = paymentService.startAlipayPayment(order);
        if(form == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        final PrintWriter writer = response.getWriter();
        response.setContentType("text/html;charset=utf-8");
        writer.write(form);
        writer.flush();
        writer.close();
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

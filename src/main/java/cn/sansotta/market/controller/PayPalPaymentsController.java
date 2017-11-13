package cn.sansotta.market.controller;

import com.paypal.api.payments.Payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.service.OrderService;
import cn.sansotta.market.service.impl.PayPalPaymentManager;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Controller
@RequestMapping("/payments/paypal")
public class PayPalPaymentsController {
    private final PayPalPaymentManager manager;
    private final OrderService orderService;
    private final static Logger logger = LoggerFactory.getLogger(PayPalPaymentManager.class);

    public PayPalPaymentsController(PayPalPaymentManager manager,
                                    OrderService orderService) {
        this.manager = manager;
        this.orderService = orderService;
    }

    @GetMapping("/return")
    public void executePayment(@RequestParam("paymentId") String paymentId,
                               @RequestParam("PayerID") String payerId,
                               HttpServletResponse response) throws IOException {
        final Payment payment = manager.executePayment(paymentId, payerId);
        if(payment == null) response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else {
            // PayPal's sdk will serve as fence here: only the first call succeed.
            // always only one thread for one order will continue from here.
            succeed(payment, response);
            response.sendRedirect("/");
        }
    }

    private void succeed(Payment payment, HttpServletResponse response) throws IOException {
        long orderId = Long.valueOf(payment.getTransactions().get(0).getReferenceId());
        Order order = orderService.paymentDone(orderId); // now order's status is STOCK_OUT

        if(order == null) { // this condition should not happen
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            logger.error("error after payment succeeding. " +
                    "PLEASE CONTACT YOUR SUPPORT MANAGER IMMEDIATELY IF YOU READ THIS MESSAGE!!");
        } else {
            response.sendRedirect("/"); // TODO: placeholder. redirect to really url.
        }

    }
}

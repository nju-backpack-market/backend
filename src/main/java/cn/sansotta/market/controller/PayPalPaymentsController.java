package cn.sansotta.market.controller;

import com.paypal.api.payments.Payment;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.domain.value.Trade;
import cn.sansotta.market.service.CommonPaymentService;
import cn.sansotta.market.service.PayPalPaymentService;
import cn.sansotta.market.service.TradeService;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Controller
@RequestMapping("/payments/paypal")
public class PayPalPaymentsController {
    private final PayPalPaymentService payPalService;
    private final CommonPaymentService paymentService;
    private final TradeService tradeService;

    public PayPalPaymentsController(PayPalPaymentService manager,
                                    CommonPaymentService paymentService,
                                    TradeService tradeService) {
        this.payPalService = manager;
        this.paymentService = paymentService;
        this.tradeService = tradeService;
    }

    @GetMapping("/cancel")
    public void cancelPayment(@RequestParam("token") String token,
                              HttpServletResponse response) throws IOException {
        Trade trade = tradeService.getTradeByExtra(token);
        // invalid response cannot be from user's browser, return nothing but error code.
        if(trade == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Order order = paymentService.paymentCancel(trade.getOid());
        if(order == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.sendRedirect("/api"); // TODO: replace placeholder.
    }


    @GetMapping("/return")
    public void executePayment(@RequestParam("paymentId") String paymentId,
                               @RequestParam("PayerID") String payerId,
                               HttpServletResponse response) throws IOException {
        final Payment payment = payPalService.executePayment(paymentId, payerId);
        if(payment == null) response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        else tryDone(payment, response);
        // PayPal's api will serve as fence here: only the first call succeed.
        // always only one thread for one order will continue from here.

    }

    private void tryDone(Payment payment, HttpServletResponse response) throws IOException {
        String tradeId = payment.getId();
        Trade trade = tradeService.getTradeByTradeIdLocked(tradeId);
        if(trade == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Order order = paymentService.paymentDone(trade.getOid()); // now order's status is STOCK_OUT

        if(order == null)  // this condition indicate concurrent payment voiding
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        else {
            tradeService.voidTradeExtra(trade.getOid());
            // I find no document about the token, so i assume it
            // will expire sometimes later? so delete it now. not mandatory so we don't care its result
            response.sendRedirect("/api"); // TODO: placeholder. redirect to really url.
        }
    }
}

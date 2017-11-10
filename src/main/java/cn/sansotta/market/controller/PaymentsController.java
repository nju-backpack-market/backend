package cn.sansotta.market.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sansotta.market.domain.value.Order;
import cn.sansotta.market.service.PaymentService;
import cn.sansotta.market.service.impl.PayPalPaymentManager;

import static cn.sansotta.market.common.HateoasUtils.notFoundResponse;
import static cn.sansotta.market.common.HateoasUtils.okResponse;
import static cn.sansotta.market.common.HateoasUtils.toResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Controller
@RequestMapping("/test_pay")
public class PaymentsController {
    private final PaymentService manager;

    @Autowired
    public PaymentsController(PayPalPaymentManager manager) {this.manager = manager;}

    @GetMapping(value = "/{id}", produces = "text/html")
    @ResponseBody
    public String pay(@PathVariable("id") long id) {
        String content = "<html><body><h3>这里应该是支付平台提供的页面。<br/>结算完成后，支付平台页面会同步地GET一个我们的" +
                "页面进行跳转，同时异步地POST一个我们的接口来通知。<br/>请在GET到的页面上（稍等一小段延迟时间后）再一次请" +
                "求查询订单的接口来确认结果<br/></h3></body></html>";
        return content;
    }

    @GetMapping
    public String
    viaPayPal() {
        Payment payment = manager.createPayment(Order.mockObject());

        if(payment == null) return "redirect:/";
        for (Links links : payment.getLinks())
            if(links.getRel().equals("approval_url"))
                return "redirect:" + links.getHref();
        return "redirect:/";
    }

    @GetMapping("/success")
    @ResponseBody
    public ResponseEntity
    payPalSucceed(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        Payment payment = manager.executePayment(paymentId, payerId);
        if(payment == null) return notFoundResponse();
        if(payment.getState().equals("approved")) return toResponse("SUCCESS!!");
        return okResponse();
    }

    @GetMapping("/cancel")
    @ResponseBody
    public ResponseEntity
    payPalCancel() {
        System.err.println("CANCELED");
        return okResponse();
    }
}

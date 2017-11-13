package cn.sansotta.market.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import cn.sansotta.market.service.AlipayPaymentService;
import cn.sansotta.market.service.OrderService;

import static cn.sansotta.market.common.WebUtils.badRequestResponse;
import static cn.sansotta.market.common.WebUtils.conflictResponse;
import static cn.sansotta.market.common.WebUtils.toResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Controller
@RequestMapping("/payments/alipay")
public class AlipayPaymentsController {
    private final AlipayPaymentService paymentService;
    private final OrderService orderService;

    public AlipayPaymentsController(AlipayPaymentService service,
                                    OrderService orderService) {
        this.paymentService = service;
        this.orderService = orderService;
    }

    @GetMapping("/return")
    public String
    syncReturn(@RequestParam Map<String, String> params) {
        if(paymentService.checkSign(params)) return "redirect:/api"; // TODO: add really redirect here
        else return "redirect:/";
    }

    @PostMapping("/notify")
    @ResponseBody
    public ResponseEntity<String>
    asyncReturn(@RequestParam Map<String, String> params) {
        long orderId = Long.valueOf(params.get("out_trade_no"));
        if(paymentService.checkSign(params) && paymentService.checkValid(params))
            if(tryDone(orderId)) return toResponse("success");
            else return conflictResponse();
        else return badRequestResponse();
    }

    private boolean tryDone(long orderId) {
        return orderService.paymentDone(orderId) != null;
    }
}

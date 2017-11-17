package cn.sansotta.market.controller;

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
import cn.sansotta.market.service.CommonPaymentService;

import static cn.sansotta.market.common.WebUtils.badRequestResponse;
import static cn.sansotta.market.common.WebUtils.toResponse;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Controller
@RequestMapping("/payments/alipay")
public class AlipayPaymentsController {
    private final AlipayPaymentService alipayService;
    private final CommonPaymentService paymentService;

    public AlipayPaymentsController(AlipayPaymentService alipayService,
                                    CommonPaymentService paymentService) {
        this.alipayService = alipayService;
        this.paymentService = paymentService;
    }

    @GetMapping("/return")
    public String
    syncReturn(@RequestParam Map<String, String> params) {
        if(alipayService.checkSign(params)) return "redirect:/api"; // TODO: add really redirect here
        else return "redirect:/";
    }

    @PostMapping("/notify")
    @ResponseBody
    public ResponseEntity<String>
    asyncReturn(@RequestParam Map<String, String> params) {
        long orderId = Long.valueOf(params.get("out_trade_no"));
        String status = params.get("trade_status");

        if(!alipayService.checkSign(params)) return badRequestResponse();

        // only message from alipay will reach here
        switch (status) {
            case "TRADE_SUCCESS":
                if(tryDone(params)) return toResponse("success");
                else return toResponse("fail");
            case "TRADE_CLOSED":
                tryCancel(orderId);
                return toResponse("success");
            default:
                return badRequestResponse();
        }
    }

    private boolean tryDone(Map<String, String> params) {
        try {
            return alipayService.handleSuccessNotification(params);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private void tryCancel(long orderId) { paymentService.paymentCancel(orderId);}
}

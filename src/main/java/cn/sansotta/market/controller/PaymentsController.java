package cn.sansotta.market.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@RequestMapping("/payments")
public class PaymentsController {
    @GetMapping(value = "/{id}",produces = "text/html")
    @ResponseBody
    public String pay(@PathVariable("id") long id) {
        String content = "<html><body><h3>这里应该是支付平台提供的页面。<br/>结算完成后，支付平台页面会同步地GET一个我们的" +
                "页面进行跳转，同时异步地POST一个我们的接口来通知。<br/>请在GET到的页面上（稍等一小段延迟时间后）再一次请" +
                "求查询订单的接口来确认结果<br/></h3></body></html>";
        return content;
    }
}

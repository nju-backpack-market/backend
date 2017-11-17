package cn.sansotta.market.configuration;

import com.paypal.api.payments.RedirectUrls;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.net.UnknownHostException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import static cn.sansotta.market.common.DecryptUtils.decrypt;
import static cn.sansotta.market.common.WebUtils.contextBaseUrl;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Configuration
@EnableConfigurationProperties(PayPalConfiguration.PayPalProperties.class)
public class PayPalConfiguration {
    @Bean
    public static PayPalApiContextFactory apiContext(PayPalProperties props,
                                                     Cipher c)
            throws BadPaddingException, IllegalBlockSizeException, PayPalRESTException {
        return new PayPalApiContextFactory(
                decrypt(props.getClientId(), c),
                decrypt(props.getSecret(), c),
                props.getMode());
    }

    @Bean
    public static RedirectUrls payPalRedirect(Environment env, PayPalProperties props)
            throws UnknownHostException {
        RedirectUrls urls = new RedirectUrls();
        urls.setCancelUrl(props.getCancelUrl());
        urls.setReturnUrl(props.getReturnUrl());
        return urls;
    }

    @ConfigurationProperties(prefix = "paypal")
    public static class PayPalProperties {
        private String mode;
        private String clientId;
        private String secret;
        private String cancelUrl;
        private String returnUrl;

        public String getMode() { return mode; }

        public void setMode(String mode) { this.mode = mode; }

        public String getClientId() { return clientId; }

        public void setClientId(String clientId) { this.clientId = clientId; }

        public String getSecret() { return secret; }

        public void setSecret(String secret) { this.secret = secret; }

        public String getCancelUrl() { return cancelUrl; }

        public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }

        public String getReturnUrl() { return returnUrl; }

        public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
    }
}

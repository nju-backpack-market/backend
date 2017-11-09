package cn.sansotta.market.configuration;

import com.paypal.base.rest.PayPalRESTException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
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

    private static String decrypt(String str, Cipher c)
            throws BadPaddingException, IllegalBlockSizeException {
        return new String(c.doFinal(Base64.getDecoder().decode(str)));
    }

    @ConfigurationProperties(prefix = "paypal")
    public static class PayPalProperties {
        private String mode;
        private String clientId;
        private String secret;

        public String getMode() { return mode; }

        public void setMode(String mode) { this.mode = mode; }

        public String getClientId() { return clientId; }

        public void setClientId(String clientId) { this.clientId = clientId; }

        public String getSecret() { return secret; }

        public void setSecret(String secret) { this.secret = secret; }
    }
}

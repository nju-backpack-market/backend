package cn.sansotta.market.configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.crypto.Cipher;

import cn.sansotta.market.Main;

import static cn.sansotta.market.common.DecryptUtils.urlDecrypy;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@EnableConfigurationProperties(AlipayConfiguration.AlipayConfigurationProperties.class)
public class AlipayConfiguration {
    @Bean
    public static AlipayClient alipayClient(AlipayConfigurationProperties props,
                                            Cipher c) {
        String alipayPublicKey = urlDecrypy(readFile(props.getAlipayPublicKeyPath()), c);
        String appPrivateKey = urlDecrypy(readFile(props.getAppPrivateKeyPath()), c);
        props.alipayPublicKey = alipayPublicKey;
        return new DefaultAlipayClient(
                props.getGateway(),
                props.getAppId(),
                appPrivateKey,
                "json",
                "utf-8",
                alipayPublicKey,
                "RSA2");
    }

    private static String readFile(String path) {
        return new BufferedReader(
                new InputStreamReader(Main.class.getClassLoader().getResourceAsStream(path)))
                .lines().collect(Collectors.joining());
    }

    @ConfigurationProperties(prefix = "alipay")
    public static class AlipayConfigurationProperties {
        private String gateway;
        private String appId;
        private String appPrivateKeyPath;
        private String alipayPublicKeyPath;
        private String alipayPublicKey;
        private String returnUrl;
        private String notifyUrl;
        private String sellerId;

        public String getGateway() { return gateway; }

        public void setGateway(String gateway) { this.gateway = gateway; }

        public String getAppId() { return appId; }

        public void setAppId(String appId) { this.appId = appId; }

        public String getAppPrivateKeyPath() {
            return appPrivateKeyPath;
        }

        public void setAppPrivateKeyPath(String appPrivateKeyPath) {
            this.appPrivateKeyPath = appPrivateKeyPath;
        }

        public String getAlipayPublicKeyPath() {
            return alipayPublicKeyPath;
        }

        public void setAlipayPublicKeyPath(String alipayPublicKeyPath) {
            this.alipayPublicKeyPath = alipayPublicKeyPath;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public String getAlipayPublicKey() {
            return alipayPublicKey;
        }

        public void setAlipayPublicKey(String alipayPublicKey) {
            this.alipayPublicKey = alipayPublicKey;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }
    }
}

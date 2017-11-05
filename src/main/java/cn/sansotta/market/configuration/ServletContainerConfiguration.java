package cn.sansotta.market.configuration;

import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static cn.sansotta.market.common.Utils.string2ByteArray;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
public class ServletContainerConfiguration {
    @Bean
    public static EmbeddedServletContainerCustomizer
    embeddedServletContainerCustomizer(SecretKey secretKey) {
        return container -> {
            decryptKeystorePassword(
                    ((AbstractConfigurableEmbeddedServletContainer) container), secretKey);
        };
    }

    private static void decryptKeystorePassword(AbstractConfigurableEmbeddedServletContainer container,
                                                SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            Ssl ssl = container.getSsl();
            String actualPassword =
                    new String(cipher.doFinal(string2ByteArray(ssl.getKeyStorePassword())));
            ssl.setKeyStorePassword(actualPassword);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

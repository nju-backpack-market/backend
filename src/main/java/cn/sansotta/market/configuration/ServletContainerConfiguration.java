package cn.sansotta.market.configuration;

import org.springframework.boot.context.embedded.AbstractConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.crypto.Cipher;

import static cn.sansotta.market.common.DecryptUtils.decrypt;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Profile("!dev_test")
@Configuration
public class ServletContainerConfiguration {
    @Bean
    public static EmbeddedServletContainerCustomizer
    embeddedServletContainerCustomizer(Cipher cipher) {
        return container -> decryptKeystorePassword(
                ((AbstractConfigurableEmbeddedServletContainer) container), cipher);
    }

    private static void decryptKeystorePassword(AbstractConfigurableEmbeddedServletContainer container,
                                                Cipher cipher) {
        Ssl ssl = container.getSsl();
        ssl.setKeyStorePassword(decrypt(ssl.getKeyStorePassword(), cipher));
        ssl.setKeyPassword(decrypt(ssl.getKeyPassword(), cipher));
    }
}

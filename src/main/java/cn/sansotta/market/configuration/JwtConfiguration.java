package cn.sansotta.market.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import cn.sansotta.market.service.TokenService;
import cn.sansotta.market.service.impl.TokenManager;
import cn.sansotta.market.service.impl.TokenManagerFacade;

import static cn.sansotta.market.common.DecryptUtils.decrypt;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@EnableConfigurationProperties(JwtConfiguration.JwtProperties.class)
public class JwtConfiguration {
    @Bean("embeddedTokenManager")
    public static TokenService tokenManager(JwtProperties properties, Cipher cipher)
            throws BadPaddingException, IllegalBlockSizeException {
        properties.setSecret(decrypt(properties.secret, cipher));
        return new TokenManager(properties);
    }

    @Primary
    @Bean
    public static TokenService tokenService(@Qualifier("embeddedTokenManager") TokenService manager) {
        return new TokenManagerFacade(manager); // to make use of cache in 'internal' method call
    }

    /**
     * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
     */
    @ConfigurationProperties(prefix = "jwt")
    public static class JwtProperties {
        private String audience;
        private String issuer;
        private String secret;
        private long expireSecs;

        public String getAudience() { return audience; }

        public void setAudience(String audience) { this.audience = audience; }

        public String getIssuer() { return issuer; }

        public void setIssuer(String issuer) { this.issuer = issuer; }

        public String getSecret() { return secret; }

        public void setSecret(String secret) { this.secret = secret; }

        public long getExpireSecs() { return expireSecs; }

        public void setExpireSecs(long expireSecs) { this.expireSecs = expireSecs; }
    }
}

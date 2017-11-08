package cn.sansotta.market.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
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

import static cn.sansotta.market.common.Utils.string2ByteArray;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {
    @Bean("embeddedTokenManager")
    public static TokenService tokenManager(Cipher cipher, JwtProperties properties)
            throws BadPaddingException, IllegalBlockSizeException {
        properties.setSecret(new String(cipher.doFinal(string2ByteArray(properties.getSecret()))));
        return new TokenManager(properties);
    }

    @Primary
    @Bean
    public static TokenService tokenService(@Qualifier("embeddedTokenManager") TokenService manager) {
        return new TokenManagerFacade(manager); // to make use of cache in 'internal' method call
    }
}

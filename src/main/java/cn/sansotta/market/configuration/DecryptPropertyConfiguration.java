package cn.sansotta.market.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ConfigurableObjectInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static cn.sansotta.market.common.IOUtils.readFromClasspath;
import static cn.sansotta.market.common.IOUtils.readFromFile;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@Profile("!dev_test")
public class DecryptPropertyConfiguration {
    @Primary
    @Bean
    @Profile({"dev_remote", "dev_deploy"})
    public static DataSourceProperties dataSourceProperties(Cipher cipher) {
        return new DecryptedDatasourceProperties(cipher);
    }

    @Bean("custom_key")
    @Profile({"dev_remote", "dev_local", "dev_hiki"})
    public static Cipher secretKeyDevRemote()
            throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException {
        return createCipher(readKey(readFromClasspath("des_key")));
    }

    @Bean
    @Profile("dev_deploy")
    public static Cipher secretKeyDevDeploy()
            throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException {
        return createCipher(readKey(readFromFile("des_key")));
    }

    private static Cipher createCipher(SecretKey secretKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher;
    }

    private static SecretKey readKey(InputStream stream) throws ClassNotFoundException, IOException {
        Logger logger = LoggerFactory.getLogger(DecryptPropertyConfiguration.class);

        try (ObjectInputStream ois = new ConfigurableObjectInputStream(stream,
                Thread.currentThread().getContextClassLoader())) {
            return (SecretKey) ois.readObject();
        } catch (ClassNotFoundException cnfe) {
            logger.error("Invalid secret key!");
            throw cnfe;
        } catch (IOException ioe) {
            logger.error("Unable read secret key!");
            throw ioe;
        }
    }
}

package cn.sansotta.market.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.ConfigurableObjectInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import javax.crypto.SecretKey;

import static cn.sansotta.market.common.IOUtils.readFromClasspath;
import static cn.sansotta.market.common.IOUtils.readFromFile;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
public class PlaceholderConfigurerConfiguration {
    @Bean("propertySourcesPlaceholderConfigurer")
    @Profile("dev_remote")
    public static PropertySourcesPlaceholderConfigurer devLocal()
            throws IOException, ClassNotFoundException {
        return new EncryptedPropertyConfigurer(readKey(readFromClasspath("des_key")));
    }

    @Bean("propertySourcesPlaceholderConfigurer")
    @Profile("dev_deploy")
    public static PropertySourcesPlaceholderConfigurer devRemote()
            throws IOException, ClassNotFoundException {
        return new EncryptedPropertyConfigurer(readKey(readFromFile("/root/des_key")));
    }

    private static SecretKey readKey(InputStream stream) throws ClassNotFoundException, IOException {
        Logger logger = LoggerFactory.getLogger(PlaceholderConfigurerConfiguration.class);

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

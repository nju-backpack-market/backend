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

import javax.crypto.SecretKey;
import javax.sql.DataSource;

import static cn.sansotta.market.common.IOUtils.readFromClasspath;
import static cn.sansotta.market.common.IOUtils.readFromFile;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
public class DecryptPropertyConfiguration {
    @Primary
    @Bean
    @Profile("dev_remote")
    public static DataSourceProperties dataSourceProperties()
            throws IOException, ClassNotFoundException {
        return new DecryptedDatasourceProperties(readKey(readFromClasspath("des_key")));
    }

    @Primary
    @Bean
    @Profile("dev_deploy")
    public static DataSourceProperties devRemote()
            throws IOException, ClassNotFoundException {
        return new DecryptedDatasourceProperties(readKey(readFromFile("/root/des_key")));
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

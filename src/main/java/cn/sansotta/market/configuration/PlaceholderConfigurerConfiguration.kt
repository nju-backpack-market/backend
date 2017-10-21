package cn.sansotta.market.configuration

import cn.sansotta.market.common.readFromClasspath
import cn.sansotta.market.common.readFromFile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.ConfigurableObjectInputStream
import java.io.InputStream
import javax.crypto.SecretKey

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
class PlaceholderConfigurerConfiguration {
    companion object {
        @Bean("propertySourcesPlaceholderConfigurer")
        @Profile("dev_remote")
        @JvmStatic
        fun devLocal()
                = EncryptedPropertyConfigurer(readKey(readFromClasspath("des_key")))

        @Bean("propertySourcesPlaceholderConfigurer")
        @Profile("dev_deploy")
        @JvmStatic
        fun devRemote()
                = EncryptedPropertyConfigurer(readKey(readFromFile("/root/des_key")))

        private fun readKey(stream: InputStream) =
                ConfigurableObjectInputStream(stream, Thread.currentThread().contextClassLoader)
                        .readObject() as SecretKey
    }
}



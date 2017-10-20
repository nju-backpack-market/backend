package cn.sansotta.market.configuration

import cn.sansotta.market.common.readFromClasspath
import cn.sansotta.market.common.readFromFile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.InputStream
import java.io.ObjectInputStream
import javax.crypto.SecretKey

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
class PlaceholderConfigurerConfiguration {
    @Bean("propertySourcesPlaceholderConfigurer")
    @Profile("dev_local")
    fun devLocal()
            = EncryptedPropertyConfigurer(readKey(readFromClasspath("des_key")))

    @Bean("propertySourcesPlaceholderConfigurer")
    @Profile("dev_remote")
    fun devRemote()
            = EncryptedPropertyConfigurer(readKey(readFromFile("/root/des_key")))

    private fun readKey(stream: InputStream) =
            ObjectInputStream(stream).use { it.readObject() as SecretKey }
}



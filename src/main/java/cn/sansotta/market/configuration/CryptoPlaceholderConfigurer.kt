package cn.sansotta.market.configuration

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class CryptoPlaceholderConfigurer : PropertyPlaceholderConfigurer() {

}

private val encryptedPrefix = "{{enc:"
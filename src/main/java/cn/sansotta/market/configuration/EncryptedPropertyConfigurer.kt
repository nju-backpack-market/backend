package cn.sansotta.market.configuration

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.env.ConfigurablePropertyResolver
import org.springframework.core.io.ClassPathResource
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class EncryptedPropertyConfigurer(key: SecretKey) : PropertySourcesPlaceholderConfigurer() {
    private val cipher = Cipher.getInstance("DES").apply { init(Cipher.DECRYPT_MODE, key) }

    override fun processProperties(beanFactoryToProcess: ConfigurableListableBeanFactory,
                                   propertyResolver: ConfigurablePropertyResolver) {
        propertyResolver.setPlaceholderPrefix(this.placeholderPrefix)
        propertyResolver.setPlaceholderSuffix(this.placeholderSuffix)
        propertyResolver.setValueSeparator(this.valueSeparator)

        val valueResolver = { strVal: String ->
            (if (ignoreUnresolvablePlaceholders) propertyResolver.resolvePlaceholders(strVal)
            else propertyResolver.resolveRequiredPlaceholders(strVal)).
                    let { if (trimValues) it.trim() else it }.
                    takeIf { it != nullValue }?.
                    let { decryptIfNecessary(it) }
        }

        doProcessProperties(beanFactoryToProcess, valueResolver)
    }

    private fun decryptIfNecessary(originalValue: String) =
            originalValue.
                    takeIf { it.startsWith(encryptedPrefix) }?.
                    substring(encryptedPrefix.length)?.
                    let { string2ByteArray(it) }?.
                    let { cipher.doFinal(it) }?.
                    let { String(it) } ?: originalValue

    private fun string2ByteArray(str: String) = str.split(" ").map { it.toByte() }.toByteArray()
}

private val encryptedPrefix = "{{enc:"


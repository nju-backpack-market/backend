package cn.sansotta.market.configuration

import cn.sansotta.market.common.string2ByteArray
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import javax.crypto.Cipher

/**
 * Customized PlaceholderConfigurer to decrypt encoded password properties.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class DecryptedDatasourceProperties(private val cipher: Cipher) : DataSourceProperties() {

    override fun setPassword(password: String) {
        super.setPassword(decryptIfNecessary(password))
    }

    private fun decryptIfNecessary(source: String) =
            source.
                    takeIf { it.startsWith(encryptedPrefix) }?.
                    substring(encryptedPrefix.length)?.
                    let { string2ByteArray(it) }?.
                    let { cipher.doFinal(it) }?.
                    let { String(it) } ?: source
}

private val encryptedPrefix = "{{enc:"


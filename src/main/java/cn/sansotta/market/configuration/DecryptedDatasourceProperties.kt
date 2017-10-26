package cn.sansotta.market.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import javax.crypto.Cipher
import javax.crypto.SecretKey

/**
 * Customized PlaceholderConfigurer to decrypt encoded password properties.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class DecryptedDatasourceProperties(key: SecretKey) : DataSourceProperties() {

    override fun setPassword(password: String) {
        super.setPassword(decryptIfNecessary(password))
    }

    private val cipher = Cipher.getInstance("DES").apply { init(Cipher.DECRYPT_MODE, key) }

    private fun decryptIfNecessary(source: String) =
            source.
                    takeIf { it.startsWith(encryptedPrefix) }?.
                    substring(encryptedPrefix.length)?.
                    let { string2ByteArray(it) }?.
                    let { cipher.doFinal(it) }?.
                    let { String(it) } ?: source

    private fun string2ByteArray(str: String) = str.split(" ").map { it.toByte() }.toByteArray()
}

private val encryptedPrefix = "{{enc:"

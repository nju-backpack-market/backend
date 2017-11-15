package cn.sansotta.market.configuration

import cn.sansotta.market.common.decrypt
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import javax.crypto.Cipher

/**
 * Customized PlaceholderConfigurer to decrypt encoded password properties.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class DecryptedDataSourceProperties(private val cipher: Cipher) : DataSourceProperties() {

    override fun setPassword(password: String) {
        super.setPassword(decrypt(password, cipher))
    }
}


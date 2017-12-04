@file:JvmName("DecryptUtils")

package cn.sansotta.market.common

import java.util.*
import javax.crypto.Cipher

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
private val decoder = Base64.getDecoder()
private val urlDecoder = Base64.getUrlDecoder()

fun decrypt(encrypted: String, c: Cipher) = String(c.doFinal(decoder.decode(encrypted)))

fun urlDecrypt(encrypted: String, c: Cipher) = String(c.doFinal(urlDecoder.decode(encrypted)))

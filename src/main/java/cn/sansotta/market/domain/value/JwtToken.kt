package cn.sansotta.market.domain.value

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class JwtToken(var token: String, var tokenType: String, var expireIn: Long) {
    constructor() : this("", "", -1)
}

fun tokenString(aud: String,
                iss: String,
                ttlMillis: Long,
                base64Security: String,
                vararg content: Pair<String, Any?>)
        = Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setIssuer(iss)
        .setAudience(aud)
        .apply { content.forEach { (key, value) -> claim(key, value) } }
        .apply {
            if (ttlMillis > 0)
                System.currentTimeMillis().let { now ->
                    setNotBefore(Date(now))
                    setExpiration(Date(now + ttlMillis))
                }
        }
        .signWith(SignatureAlgorithm.HS256,
                SecretKeySpec(
                        DatatypeConverter.parseBase64Binary(base64Security),
                        SignatureAlgorithm.HS256.jcaName))
        .compact()

fun parseJwt(token: String, secret: String) =
        try {
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                    .parseClaimsJws(token).body
                    .mapValues { (_, v) -> v.toString() }
        } catch (ex: Exception) {
            null
        }

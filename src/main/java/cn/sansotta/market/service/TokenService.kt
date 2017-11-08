package cn.sansotta.market.service

import cn.sansotta.market.domain.value.JwtToken

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface TokenService {
    fun createToken(name: String): JwtToken
    fun verifyToken(token: String?): Boolean
    fun deleteToken(name: String)
    fun getToken(name: String): JwtToken?
    fun extractName(token: String): String?
}
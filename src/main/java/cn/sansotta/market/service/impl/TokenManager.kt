package cn.sansotta.market.service.impl

import cn.sansotta.market.configuration.JwtConfiguration.JwtProperties
import cn.sansotta.market.domain.value.JwtToken
import cn.sansotta.market.domain.value.parseJwt
import cn.sansotta.market.domain.value.tokenString
import cn.sansotta.market.service.Authorized
import cn.sansotta.market.service.TokenService
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class TokenManagerFacade(tokenManager: TokenService)
    : TokenService by tokenManager, HandlerInterceptorAdapter() {
    override fun verifyToken(token: String?)
            = token?.let(this::extractName)
            ?.let(this::getToken)
            ?.let { real -> real.token == token } ?: false

    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           handler: Any): Boolean {
        if (handler !is HandlerMethod || !handler.hasMethodAnnotation(Authorized::class.java))
            return true
        val needIntercept = handler.getMethodAnnotation(Authorized::class.java).intercept
        val result = verifyToken(trimHeader(request.getHeader("Authorization")))
        request.setAttribute("authorized", result)
        if (needIntercept && !result) response.status = SC_UNAUTHORIZED
        return (!needIntercept) || result
    }

    private fun trimHeader(authorization: String?): String? {
        if (authorization == null) return null
        if (authorization.startsWith("Bearer ")) return authorization.substring(7)
        return authorization
    }
}

@CacheConfig(cacheNames = arrayOf("tokenCache"))
class TokenManager(jwtProperties: JwtProperties) : TokenService {
    private val aud = jwtProperties.audience
    private val iss = jwtProperties.issuer
    private val exp = jwtProperties.expireSecs
    private val secret = jwtProperties.secret
    private val customLoad = "username"

    @CachePut(key = "#p0")
    override fun createToken(name: String)
            = JwtToken(tokenString(aud, iss, exp * 1000, secret, customLoad to name), "bearer", exp)

    @Cacheable(key = "#p0")
    override fun getToken(name: String): JwtToken? = null

    @CacheEvict(key = "#p0")
    override fun deleteToken(name: String) = Unit

    override fun verifyToken(token: String?) = false

    override fun extractName(token: String) = parseJwt(token, secret)?.get(customLoad)

}
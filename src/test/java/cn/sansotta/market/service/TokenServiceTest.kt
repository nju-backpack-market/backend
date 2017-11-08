package cn.sansotta.market.service

import cn.sansotta.market.service.TokenService
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

/**
 * @author [tinker](mailto:tinker19981@hotmail.com)
 */
@SpringBootTest
@RunWith(SpringRunner::class)
@ActiveProfiles("dev_local")
@DirtiesContext
class TokenServiceTest {
    @Autowired
    private lateinit var tokenService: TokenService

    @Test
    fun testToken() {
        val name = "Wesley"
        val token = tokenService.createToken(name)
        assertNotNull(token)
        assertTrue(tokenService.verifyToken(token.token))
        tokenService.deleteToken(name)
        assertFalse(tokenService.verifyToken(token.token))
        assertFalse(tokenService.verifyToken("1234567890"))
    }
}
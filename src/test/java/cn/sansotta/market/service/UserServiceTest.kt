package cn.sansotta.market.service

import cn.sansotta.market.domain.value.User
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlConfig
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@SpringBootTest
@RunWith(SpringRunner::class)
@Transactional
@SqlConfig(encoding = "UTF8")
@ActiveProfiles("dev_local")
@Sql("classpath:test_schema.sql", "classpath:users_data.sql")
class UserServiceTest : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    private lateinit var service: UserService

    @Test
    fun newUser() {
        val newUser = User("MOCK4", "123456")
        assertTrue(service.newUser(newUser))
        assertEquals("123456", service.user("MOCK4")?.password)
        assertFalse(service.newUser(newUser))
        newUser.username = ""
        assertFalse(service.newUser(newUser))
        newUser.username = "MOCK5"
        newUser.password = ""
        assertFalse(service.newUser(newUser))
        assertEquals(4, countRowsInTable("users"))
    }

    @Test
    fun removeUser() {
        assertTrue(service.removeUser("MOCK1"))
        assertFalse(service.removeUser("MOCK4"))
        assertEquals(2, countRowsInTable("users"))
    }

    @Test
    fun modifyPassword() {
        assertTrue(service.modifyPassword("MOCK1", "654321"))
        assertEquals("654321", service.user("MOCK1")?.password)
    }

    @Test
    fun user() {
        assertEquals("123456", service.user("MOCK1")?.password)
        assertNull(service.user("MOCK4"))
        assertNull(service.user(""))
    }

    @Test
    fun allUsers() {
        assertEquals(3, service.allUsers(0)?.size)
    }

    @Test
    fun checkPassword() {
        assertTrue(service.checkPassword("MOCK1", "123456"))
        assertFalse(service.checkPassword("MOCK2", "654321"))
        assertFalse(service.checkPassword("MOCK4", "123456"))
        assertFalse(service.checkPassword("", "123456"))
        assertFalse(service.checkPassword("MOCK1", ""))
    }
}

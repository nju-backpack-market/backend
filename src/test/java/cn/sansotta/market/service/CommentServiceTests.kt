package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Comment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("dev_test")
@Transactional
@SqlConfig(encoding = "UTF8")
@Sql("classpath:test_schema.sql", "classpath:comments_data.sql")
class CommentServiceTests : AbstractTransactionalJUnit4SpringContextTests() {
    @Autowired
    lateinit var service: CommentService

    @Test
    fun comment() {
        assertNull(service.comment(-1, 2))
        assertNull(service.comment(2, -1))
        val comment = service.comment(1, 1)
        assertEquals(Comment(1, 1, 4, "GOOD"), comment)
    }

    @Test
    fun commentsOfProduct() {
        assertEquals(3, service.commentsOfProduct(1, 0).size)
    }

    @Test
    fun addComments() {
        val list = service.addComments(listOf(
                Comment(2, 3, 3, "Well..."),
                Comment(3, 4, 2, "Hmmm"),
                Comment(-1, 4, 4, "Error"),
                Comment(4, 2, 0, "Error")))
        assertEquals(2, list.size)
        assertEquals(9, countRowsInTable("comments"))
        assertEquals(2, service.commentsOfProduct(3, 0).size)
    }
}
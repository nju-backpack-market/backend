package cn.sansotta.market.service.impl

import cn.sansotta.market.common.copyPageInfo
import cn.sansotta.market.common.ifTrue
import cn.sansotta.market.dao.UserDao
import cn.sansotta.market.domain.entity.UserEntity
import cn.sansotta.market.domain.value.User
import cn.sansotta.market.service.TokenService
import cn.sansotta.market.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class UserManager(private val userDao: UserDao, private val tokenService: TokenService) : UserService {
    private val logger = LoggerFactory.getLogger(UserManager::class.java)

    override fun checkPassword(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) return false
        return user(username)?.let { it.password == password } ?: false
    }

    override fun newUser(user: User)
            = user.takeIf(User.Companion::isValidEntity)?.takeIf { user(user.username) == null }
            ?.let { hazard("create user") { userDao.insertUser(it.toEntity()) } } != null

    override fun removeUser(username: String)
            = username.takeIf(String::isNotBlank)
            ?.let { hazard("delete user", false) { userDao.deleteUser(it) } }
            ?.ifTrue { tokenService.deleteToken(username) } ?: false

    override fun modifyPassword(username: String, password: String): Boolean
            = username.takeIf(String::isNotBlank)
            ?.let { hazard("modify user") { userDao.updateUser(UserEntity(it, password)) } }
            ?.let { tokenService.deleteToken(username) } != null

    override fun user(username: String)
            = username.takeIf(String::isNotBlank)?.let(userDao::selectUserByUsername)?.let(::User)

    override fun allUsers(page: Int)
            = page.takeIf { it >= 0 }?.let(userDao::selectAllUsers)?.let { copyPageInfo(it, ::User) }

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method caused by $ex")
                null
            }

}
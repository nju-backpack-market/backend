package cn.sansotta.market.service

import cn.sansotta.market.domain.value.User
import com.github.pagehelper.PageInfo

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface UserService {
    fun newUser(user: User): Boolean
    fun removeUser(username: String): Boolean
    fun modifyPassword(username: String, password: String): Boolean
    fun user(username: String): User?
    fun allUsers(page: Int): PageInfo<User>?
    fun checkPassword(username: String, password: String): Boolean
}
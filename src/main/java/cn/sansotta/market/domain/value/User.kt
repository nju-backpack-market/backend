package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.UserEntity

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class User(
        var username: String,
        var password: String
) : ValueObject<UserEntity> {
    constructor() : this("", "")

    constructor(po: UserEntity) : this(po.username, po.password)

    override fun toEntity() = UserEntity(username, password)

    companion object {
        @JvmStatic
        fun isValidEntity(user: User) = user.username.isNotBlank() && user.password.isNotBlank()
    }
}
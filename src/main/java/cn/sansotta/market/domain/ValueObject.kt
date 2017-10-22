package cn.sansotta.market.domain

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface ValueObject<out Po> {
    fun toEntity(): Po
}
package cn.sansotta.market.domain.entity

/**
 * PO for Price.
 *
 * It's never shared by different instance and always embedded in one, so has no physical table.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class PriceEntity(
        val origin: Double,
        val actual: Double,
        val reason: String
)
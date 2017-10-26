package cn.sansotta.market.domain.entity

/**
 * Po for DeliveryInfo.
 *
 * It's never shared by different instance and always embedded in one, so has no physical table.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class DeliveryInfoEntity(
        val name: String,
        val phoneNumber: String,
        val email: String,
        val address: String
)
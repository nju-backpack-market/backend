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
        val country: String, // reserve for future use
        val city: String,
        val province: String,
        /**
         * The first line of the address. For example, number, street, and so on. Maximum length is 100 characters.
         * */
        val addressLine1: String,
        /**
         * The second line of the address. For example, suite, apartment number, and so on. Maximum length is 100 characters.
         * */
        val addressLine2: String,
        val postalCode: String
)
package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.DeliveryInfoEntity

/**
 * Information about delivery.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class DeliveryInfo(
        /**
         * Customer's name.
         * */
        var name: String,
        /**
         * Customer's phone number.
         *  */
        var phoneNumber: String,
        /**
         * Customer's email.
         * */
        var email: String,
        /**
         * Customer's country.
         * For future use.
         */
        var country: String,
        /**
         * Customer's province.
         * */
        var province: String,
        /**
         * Customer's city.
         * */
        var city: String,
        /**
         * The first line of the address. For example, number, street,
         * and so on. Maximum length is 100 characters.
         * */
        var addressLine1: String,
        /**
         * The second line of the address. For example, suite, apartment number,
         * and so on. Maximum length is 100 characters.
         * */
        var addressLine2: String,
        /**
         * Postal code.
         * */
        var postalCode: String,
        var logisticCompany: String?,
        var logisticCode: String?
) : ValueObject<DeliveryInfoEntity> {
    constructor() : this("", "", "", "", "", "", "", "", "", null, null)

    constructor(po: DeliveryInfoEntity) : this(po.name, po.phoneNumber, po.email, po.country,
            po.province, po.city, po.addressLine1, po.addressLine2, po.postalCode, po.logisticCompany,
            po.logisticCode)

    override fun toEntity() = DeliveryInfoEntity(name, phoneNumber, email, country, province, city,
            addressLine1, addressLine2, postalCode, logisticCompany, logisticCode)

    companion object {
        @JvmStatic
        fun mockObject() = DeliveryInfo("qinliu", "1234567890123", "qinliu@software.com", "China",
                "JiangSu", "Nanjing", "nju", "se institution", "260023", "顺丰", "123456789012")

        @JvmStatic
        fun isValidEntity(info: DeliveryInfo): Boolean {
            return info.name.isNotBlank() && info.phoneNumber.isNotBlank() &&
                    info.city.isNotBlank() && info.addressLine1.isNotBlank() &&
                    info.addressLine2.isNotBlank() && info.postalCode.isNotBlank()
        }

        @JvmStatic
        fun mergeAsUpdate(origin: DeliveryInfoEntity, modified: DeliveryInfo): DeliveryInfo {
            if (modified.name.isBlank()) modified.name = origin.name
            if (modified.phoneNumber.isBlank()) modified.phoneNumber = origin.phoneNumber
            if (modified.email.isBlank()) modified.email = origin.email
            if (modified.city.isBlank()) modified.city = origin.city
            if (modified.addressLine1.isBlank()) modified.addressLine1 = origin.addressLine1
            if (modified.addressLine2.isBlank()) modified.addressLine2 = origin.addressLine2
            if (modified.postalCode.isBlank()) modified.postalCode = origin.postalCode
            if (modified.logisticCompany == null) modified.logisticCompany = origin.logisticCompany
            if (modified.logisticCode == null) modified.logisticCode = origin.logisticCode
            return modified
        }
    }
}
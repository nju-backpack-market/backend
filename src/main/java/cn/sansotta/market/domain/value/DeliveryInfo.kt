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
         * Customer's address.
         * */
        var address: String
) : ValueObject<DeliveryInfoEntity> {
    constructor() : this("", "", "", "")

    constructor(po: DeliveryInfoEntity) : this(po.name, po.phoneNumber, po.email, po.address)

    override fun toEntity() = DeliveryInfoEntity(name, phoneNumber, email, address)
}
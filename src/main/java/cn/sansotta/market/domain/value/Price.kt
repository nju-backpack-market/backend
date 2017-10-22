package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.PriceEntity
import com.fasterxml.jackson.annotation.JsonInclude

/**
 * Price.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class Price(
        /**
         * Original totalPrice.
         *
         * This field holds the very original totalPrice without any discount, while field [actual] means
         * totalPrice with all possible discount.
         *
         * For example, assume that a certain production is $300 per each originally.
         * The applicable discount is 20% off per each, buy-one, get-one-free,
         * and $20 off once the whole bill totalPrice up to $200. Now if we buy two, then
         * * the origin unit totalPrice is $300, actual unit totalPrice is $240
         * * the origin subtotal totalPrice is $600, actual subtotal totalPrice is $240
         * * the origin total totalPrice is $600, actual subtotal totalPrice is $220
         * */
        var origin: Double,
        /**
         * Actual totalPrice. Null if no discount.
         *
         * See [origin]'s document for detail.
         * */
        @get:JsonInclude(JsonInclude.Include.NON_NULL)
        var actual: Double?
) : ValueObject<PriceEntity> {
    constructor() : this(0.0, null)

    constructor(origin: Double) : this(origin, null)

    constructor(po: PriceEntity) : this(po.origin, po.actual)

    override fun toEntity() = PriceEntity(origin, actual)
}
package cn.sansotta.market.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect

/**
 * Shopping item in shopping list.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ShoppingItem(
        /**
         * Id of production.
         * */
        var pid: Int,
        /**
         * Count of production.
         * */
        var count: Int,
        /**
         * Unit totalPrice of each.
         * */
        private val unitPrice: Price,
        /**
         * Subtotal totalPrice of this item.
         *
         * Note that it can differ from count * unitPrice,
         * because of sale promotion strategy.
         * */
        private val subtotalPrice: Price
) {
    constructor() : this(0, 0, Price(), Price())

    constructor(pid: Int, count: Int, originUnitPrice: Int)
            : this(pid, count, Price(originUnitPrice), Price())

    var originalUnitPrice
        get() = unitPrice.origin
        set(value) {
            unitPrice.origin = value
            subtotalPrice.origin = originalSubtotalPrice
        }
    val originalSubtotalPrice get() = originalUnitPrice * count
    val defaultSubtotalPrice get() = actualUnitPrice?.times(count)

    var actualUnitPrice
        get() = unitPrice.actual
        set(value) {
            unitPrice.actual = value
        }
    var actualSubtotalPrice
        get() = subtotalPrice.actual
        set(value) {
            subtotalPrice.actual = value
        }

    var unitPriceDiscountReason
        get() = unitPrice.reason
        set(value) {
            unitPrice.reason = value
        }
    var subtotalPriceDiscountReason
        get() = subtotalPrice.reason
        set(value) {
            subtotalPrice.reason = value
        }
}
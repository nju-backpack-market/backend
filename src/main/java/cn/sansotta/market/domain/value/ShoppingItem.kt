package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.ShoppingItemEntity
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
        var pid: Long,
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
) : ValueObject<ShoppingItemEntity> {
    constructor() : this(0L, 0, Price(), Price())

    constructor(pid: Long, count: Int, originUnitPrice: Double)
            : this(pid, count, Price(originUnitPrice), Price())

    constructor(po: ShoppingItemEntity)
            : this(po.pid, po.count, Price(po.unitPrice), Price(po.subtotalPrice))

    var originalUnitPrice
        get() = unitPrice.origin
        set(value) {
            unitPrice.origin = value
            subtotalPrice.origin = originalSubtotalPrice // a trigger to update subtotalPrice.origin
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

    override fun toEntity() =
            ShoppingItemEntity(pid, count, unitPrice.toEntity(), subtotalPrice.toEntity())
}
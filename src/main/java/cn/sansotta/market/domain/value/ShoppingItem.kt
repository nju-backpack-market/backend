package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.ShoppingItemEntity
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.hateoas.core.Relation

/**
 * Shopping item in shopping list.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Relation("shopping_item", collectionRelation = "shopping_list")
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
        @field:JsonUnwrapped(suffix = "_unit_price")
        private val unitPrice: Price,
        /**
         * Subtotal totalPrice of this item.
         *
         * Note that it can differ from count * unitPrice,
         * because of sale promotion strategy.
         * */
        @field:JsonUnwrapped(suffix = "_subtotal_price")
        private val subtotalPrice: Price
) : ValueObject<ShoppingItemEntity> {
    constructor() : this(0L, 0, Price(), Price())

    constructor(pid: Long, count: Int, originUnitPrice: Double)
            : this(pid, count, Price(originUnitPrice), Price())

    constructor(po: ShoppingItemEntity)
            : this(po.pid, po.count, Price(po.unitPrice),
            Price(po.count * po.unitPrice.origin, po.subtotalPrice)) // calculate origin price manually

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
            ShoppingItemEntity(pid, count, unitPrice.toEntity(),
                    subtotalPrice.actual ?: unitPrice.origin * count)
}
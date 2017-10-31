package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.ShoppingItemEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.hateoas.core.Relation

/**
 * Shopping item in shopping list.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
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
        private val unitPrice: Price,
        /**
         * Subtotal totalPrice of this item.
         *
         * Note that it can differ from count * unitPrice,
         * because of sale promotion strategy.
         * */
        private val subtotalPrice: Price
) : ValueObject<ShoppingItemEntity> {
    constructor() : this(-1L, -1, Price(), Price())

    constructor(pid: Long, count: Int, originUnitPrice: Double)
            : this(pid, count, Price(originUnitPrice), Price())

    constructor(po: ShoppingItemEntity)
            : this(po.pid, po.count, Price(po.unitPrice),
            Price(po.count * po.unitPrice.origin, po.subtotalPrice)) // calculate origin price manually

    var originUnitPrice
        get() = unitPrice.origin
        set(value) {
            unitPrice.origin = value
            subtotalPrice.origin = originSubtotalPrice // a trigger to update subtotalPrice.origin
        }
    val originSubtotalPrice get() = originUnitPrice * count
    @get:JsonIgnore
    val defaultSubtotalPrice
        get() = actualUnitPrice?.times(count)

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var actualUnitPrice
        get() = unitPrice.actual
        set(value) {
            unitPrice.actual = value
        }
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var actualSubtotalPrice
        get() = subtotalPrice.actual
        set(value) {
            subtotalPrice.actual = value
        }

    override fun toEntity() =
            ShoppingItemEntity(pid, -1L, count, unitPrice.toEntity(),
                    subtotalPrice.actual ?: unitPrice.origin * count)

    companion object {
        @JvmStatic
        fun mockObject() = ShoppingItem(114514, 1919, 81.0)

        @JvmStatic
        fun isValidEntity(item: ShoppingItem) = item.count > 0 &&
                Price.isValidEntity(item.unitPrice) &&
                Price.isValidEntity(item.subtotalPrice)
    }
}
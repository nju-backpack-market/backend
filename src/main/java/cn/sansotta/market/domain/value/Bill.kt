package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.BillEntity
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Shopping list of an order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)
class Bill() : Iterable<ShoppingItem>, ValueObject<BillEntity> {
    /* shopping list of shopping item in the bill */
    private val shoppingList = mutableListOf<ShoppingItem>()
        @SuppressWarnings get() = field // only by this can Jackson scan this getter, weird
    /* total price */
    private var totalPrice = Price()
        get() {
            field.origin = originTotalPrice // lazy calculation of origin price
            return field
        }
        set(value) {
            field.origin = value.origin
            field.actual = value.actual
        }

    constructor(list: List<ShoppingItem>, totalPrice: Price) : this() {
        this.shoppingList.addAll(list)
        this.totalPrice = totalPrice
    }

    constructor(list: List<ShoppingItem>) : this() {
        this.shoppingList.addAll(list)
    }

    constructor(vararg items: ShoppingItem) : this() {
        this.shoppingList.addAll(items)
    }

    constructor(po: BillEntity)
            : this(po.shoppingList.map { ShoppingItem(it) }, Price(po.totalPrice))

    val originTotalPrice
        @JsonIgnore get() = shoppingList.sumByDouble { it.originalSubtotalPrice }
    var actualTotalPrice
        @JsonIgnore get() = totalPrice.actual
        set(value) {
            totalPrice.actual = value
        }

    fun addShoppingItem(item: ShoppingItem) {
        shoppingList.add(item)
    }

    override fun iterator() = shoppingList.iterator()

    override fun toEntity() =
            BillEntity(shoppingList.map { it.toEntity() }, totalPrice.toEntity())
}

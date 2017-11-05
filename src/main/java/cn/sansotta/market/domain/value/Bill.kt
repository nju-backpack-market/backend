package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.value.ShoppingItem.Companion.isValidEntity
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

/**
 * Shopping list of an order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
// can only detect through getter 'cause of trigger in [totalPrice]'s getter
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)
class Bill() : Iterable<ShoppingItem>, Cloneable {
    /* shopping list of shopping item in the bill */
    private val shoppingList = mutableListOf<ShoppingItem>()
    //        @SuppressWarnings get() = field // it's the only way can hint jackson detect this, wired
    /* total price */
    @get:JsonUnwrapped(suffix = "_total_price")
    private var totalPrice = Price()
        get() {
            field.origin = originTotalPrice // lazy calculation of origin price
            return field
        }
        set(value) {
            field.origin = value.origin
            field.actual = value.actual
        }

    constructor(items: Iterable<ShoppingItem>, totalPrice: Price) : this(items) {
        this.totalPrice = totalPrice
    }

    constructor(@JsonProperty("shopping_list") list: Iterable<ShoppingItem>) : this() {
        this.shoppingList.addAll(list)
    }

    constructor(vararg items: ShoppingItem) : this() {
        this.shoppingList.addAll(items)
    }

    override fun toString() = "Bill(shoppingList=$shoppingList, totalPrice=$totalPrice)"

    @get:JsonIgnore
    val originTotalPrice
        get() = shoppingList.sumByDouble { it.originSubtotalPrice }
    @get:JsonIgnore
    var actualTotalPrice
        get() = totalPrice.actual ?: originTotalPrice
        set(value) {
            totalPrice.actual = value
        }

    fun addShoppingItem(item: ShoppingItem) {
        shoppingList.add(item)
    }

    override fun iterator() = shoppingList.iterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bill) return false

        if (this.totalPrice != other.totalPrice) return false
        if (this.shoppingList != other.shoppingList) return false
        return true
    }

    override fun hashCode() = this.shoppingList.hashCode() * 37 + this.totalPrice.hashCode()

    public override fun clone(): Bill {
        val new = super.clone() as Bill
        new.totalPrice = this.totalPrice.copy()
        new.shoppingList.addAll(this.shoppingList.map { it.copy() })
        return new
    }

    companion object {
        @JvmStatic
        fun mockObject() = Bill(ShoppingItem.mockObject(), ShoppingItem.mockObject().copy(pid = 2333))

        @JvmStatic
        fun isValidEntity(bill: Bill)
                = bill.actualTotalPrice >= 0.0 &&
                bill.shoppingList.all(ShoppingItem.Companion::isValidEntity) &&
                bill.shoppingList.distinctBy { it.pid }.size == bill.shoppingList.size
    }
}

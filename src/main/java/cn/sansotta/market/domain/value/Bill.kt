package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.BillEntity
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Shopping list of an order.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class Bill() : Iterable<ShoppingItem>, ValueObject<BillEntity>, Cloneable {
    /* shopping list of shopping item in the bill */
    private val shoppingList = mutableListOf<ShoppingItem>()
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

    constructor(items: Iterable<ShoppingItem>, totalPrice: Price) : this(items) {
        this.totalPrice = totalPrice
    }


    constructor(@JsonProperty("shopping_list") list: Iterable<ShoppingItem>) : this() {
        this.shoppingList.addAll(list)
    }

    constructor(vararg items: ShoppingItem) : this() {
        this.shoppingList.addAll(items)
    }

    constructor(po: BillEntity) : this(po.shoppingList.map { ShoppingItem(it) }) {
        // calculate origin price from shopping list and total price from po
        this.totalPrice = Price(originTotalPrice, po.totalPrice)
    }

    override fun toString() = "Bill(shoppingList=$shoppingList, totalPrice=$totalPrice)"

    val originTotalPrice
        get() = shoppingList.sumByDouble { it.originSubtotalPrice }
    var actualTotalPrice
        get() = totalPrice.actual
        set(value) {
            totalPrice.actual = value
        }

    fun addShoppingItem(item: ShoppingItem) {
        shoppingList.add(item)
    }

    override fun iterator() = shoppingList.iterator()

    override fun toEntity() =
            BillEntity(shoppingList.map { it.toEntity() }, totalPrice.actual)

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
    }
}

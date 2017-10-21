package cn.sansotta.market.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.Identifiable

/**
 * Shopping shoppingList of an order.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)
class Bill() : Iterable<ShoppingItem>, Identifiable<Int> {
    /**
     * Bill's id.
     *
     * -1 if the bill doesn't relate to an order, otherwise the same as order's id.
     * */
    var orderId = -1

    /**
     * For hypermedia use.
     * */
    override fun getId() = orderId

    /* shopping list of shopping item in the bill */
    private val shoppingList = mutableListOf<ShoppingItem>()
        @SuppressWarnings @JsonProperty("shopping_list") get() = field
    /* total price */
    private val totalPrice: Price = Price()
        get() {
            field.origin = originTotalPrice // lazy calculation of origin price
            return field
        }

    constructor(list: List<ShoppingItem>) : this() {
        this.shoppingList.addAll(list)
    }

    constructor(vararg items: ShoppingItem) : this() {
        this.shoppingList.addAll(items)
    }

    val originTotalPrice
        @JsonIgnore get() = shoppingList.sumBy { it.originalSubtotalPrice }
    var actualTotalPrice
        @JsonIgnore get() = totalPrice.actual
        set(value) {
            totalPrice.actual = value
        }
    var billDiscountReason
        @JsonIgnore get() = totalPrice.reason
        set(value) {
            totalPrice.reason = value
        }

    fun addShoppingItem(item: ShoppingItem) {
        shoppingList.add(item)
    }

    override fun iterator() = shoppingList.iterator()
}

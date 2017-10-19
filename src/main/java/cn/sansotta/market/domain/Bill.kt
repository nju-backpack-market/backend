package cn.sansotta.market.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Shopping list of an order.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)
class Bill : Iterable<ShoppingItem> {
    private val list = mutableListOf<ShoppingItem>()
        @SuppressWarnings @JsonProperty("shopping_list") get() = field
    private val totalPrice: Price = Price()
        get() {
            field.origin = originTotalPrice
            return field
        }

    constructor(list: List<ShoppingItem>) {
        this.list.addAll(list)
    }

    constructor(vararg items: ShoppingItem) {
        this.list.addAll(items)
    }

    val originTotalPrice
        @JsonIgnore get() = list.sumBy { it.originalSubtotalPrice }
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
        list.add(item)
    }

    override fun iterator() = list.iterator()
}

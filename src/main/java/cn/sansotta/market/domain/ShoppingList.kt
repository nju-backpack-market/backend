package cn.sansotta.market.domain

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Shopping list of an order.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ShoppingList {
    val list: MutableList<ShoppingItem>
        @JsonProperty("shopping_list") get

    init {
        list = mutableListOf()
    }

    constructor(list: List<ShoppingItem>) {
        this.list.addAll(list)
    }

    constructor(vararg items: ShoppingItem) {
        this.list.addAll(items)
    }

    val totalPrice get() = list.sumBy { it.subtotalPrice }
}

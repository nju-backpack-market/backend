package cn.sansotta.market.domain.entity

/**
 * Po for ShoppingItem.
 *
 * Table: product(pid, pname, price, description)
 *
 * @author Hiki
 */
data class ProductEntity(val id: Long,
                         val name: String,
                         val price: Double,
                         val description: String,
                         val images: List<String>,
                         val onSale: Boolean) {
    constructor(id: Long, name: String, price: Double, description: String, onSale: Boolean)
            : this(id, name, price, description, emptyList(), onSale)
}

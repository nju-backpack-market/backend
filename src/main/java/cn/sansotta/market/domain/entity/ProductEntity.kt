package cn.sansotta.market.domain.entity

/**
 * Po for ShoppingItem.
 *
 * Table: product(pid, pname, price, description)
 *
 * @author Hiki
 */
data class ProductEntity(var id: Long,
                         var name: String,
                         var price: Double,
                         var description: String,
                         var images: List<String>)

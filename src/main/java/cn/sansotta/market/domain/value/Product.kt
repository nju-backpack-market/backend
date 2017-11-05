package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.ProductEntity
import org.springframework.hateoas.Identifiable
import org.springframework.hateoas.core.Relation

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Relation("product", collectionRelation = "products")
data class Product(
        var pid: Long,
        var name: String,
        var price: Double,
        var description: String,
        var images: List<String>
) : ValueObject<ProductEntity>, Identifiable<Long> {
    override fun getId() = pid

    constructor() : this(-1, "", -1.0, "", mutableListOf())

    constructor(po: ProductEntity) : this(po.id, po.name, po.price, po.description, po.images)

    override fun toEntity() = ProductEntity(pid, name, price, description, images)

    companion object {
        @JvmStatic
        fun mockObject() = Product(1, "MockProduct", 10000.0, "MOCK", mutableListOf())

        @JvmStatic
        fun isValidEntity(product: Product) = product.name.isNotBlank() && product.price >= 0.0

        @JvmStatic
        fun mergeAsUpdate(origin: ProductEntity, modified: Product): Product? {
            if (origin.id != modified.id) return null

            if (modified.name.isBlank())
                modified.name = origin.name
            if (modified.price < 0.0)
                modified.price = origin.price
            return modified
        }
    }
}
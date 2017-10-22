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
        var id: Long,
        var name: String,
        var price: Double,
        var description: String
) : ValueObject<ProductEntity>, Identifiable<Long> {
    override fun getId() = id

    constructor() : this(-1, "", -1.0, "")

    constructor(po: ProductEntity) : this(po.id, po.name, po.price, po.description)

    override fun toEntity() = ProductEntity(id, name, price, description)
}
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
        var description: String
) : ValueObject<ProductEntity>, Identifiable<Long> {
    override fun getId() = pid

    constructor() : this(-1, "", -1.0, "")

    constructor(po: ProductEntity) : this(po.id, po.name, po.price, po.description)

    override fun toEntity() = ProductEntity(pid, name, price, description)

    companion object {
        @JvmStatic
        fun mockObject() = Product(1, "MockProduct", 10000.0, "MOCK")
    }
}
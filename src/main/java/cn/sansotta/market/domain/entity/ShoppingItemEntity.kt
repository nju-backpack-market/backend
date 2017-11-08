package cn.sansotta.market.domain.entity

/**
 * Po for ShoppingItem.
 *
 * Table: shopping_item(pid, oid, count, unit_origin, unit_actual, unit_discount, subtotal_origin,
 * subtotal_actual, subtotal_discount)
 *
 * [unitPrice] and [subtotalPrice] is simple wrapper. They are embedded in ShoppingItemEntity's table.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class ShoppingItemEntity(
        val pid: Long,
        val count: Int,
        val unitPrice: PriceEntity,
        val subtotalPrice: Double)
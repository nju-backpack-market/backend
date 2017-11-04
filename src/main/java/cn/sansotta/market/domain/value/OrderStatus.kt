package cn.sansotta.market.domain.value

/**
 * Order's state.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
enum class OrderStatus(vararg validTransfer: OrderStatus) {
    INVALID(),
    DELIVERING(),
    STOCK_OUT(DELIVERING),
    CREATE(STOCK_OUT);

    private val validTransfer = setOf(*validTransfer)

    fun isValidTransfer(transfer: OrderStatus) = transfer in validTransfer
}
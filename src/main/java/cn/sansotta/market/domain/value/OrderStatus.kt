package cn.sansotta.market.domain.value

/**
 * Order's state.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
enum class OrderStatus {
    INVALID,
    DELIVERING,
    STOCK_OUT,
    PAYING,
    CREATE;

    fun canTransferTo(transfer: OrderStatus) =
            when (this) {
                CREATE -> transfer == PAYING
                PAYING -> transfer == CREATE || transfer == STOCK_OUT
                else -> false
            }
}
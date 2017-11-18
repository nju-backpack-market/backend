package cn.sansotta.market.domain.value

/**
 * Order's state.
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
enum class OrderStatus {
    CREATE, // 已创建
    PAYING, // 正在付款
    STOCK_OUT,  // 已付款，正在出库
    DELIVERING,  // 正在配送
    SIGNED,  // 已签收
    COMMENTED, // 已评论
    COMPLETED, // 已完成
    //    APPEALING, // 申诉中
    CLOSED,  // 已关闭
    INVALID;

    fun canTransferTo(transfer: OrderStatus) =
            when (this) {
                CREATE -> transfer == PAYING || transfer == CLOSED
                PAYING -> transfer == CREATE || transfer == STOCK_OUT
                STOCK_OUT -> transfer == DELIVERING // || transfer == APPEALING
                DELIVERING -> transfer == SIGNED
                SIGNED -> transfer == COMMENTED // || transfer == APPEALING
                COMMENTED -> transfer == COMPLETED // || transfer == APPEALING
//                APPEALING -> transfer == CLOSED
                else -> false
            }
}
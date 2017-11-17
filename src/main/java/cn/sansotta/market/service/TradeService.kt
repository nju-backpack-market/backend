package cn.sansotta.market.service

import cn.sansotta.market.domain.value.Trade

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
interface TradeService {
    fun newTrade(trade: Trade): Boolean
    fun getTradeId(orderId: Long): String?
    fun getTradeByExtra(extra: String): Trade?
    fun getTradeLocked(orderId: Long): Trade?
    fun getTradeByTradeIdLocked(tradeId: String): Trade?
    fun voidTrade(orderId: Long): Boolean
    fun voidTradeExtra(orderId: Long)
}
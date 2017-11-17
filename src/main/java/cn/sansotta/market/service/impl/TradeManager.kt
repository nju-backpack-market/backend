package cn.sansotta.market.service.impl

import cn.sansotta.market.dao.TradeDao
import cn.sansotta.market.domain.value.Trade
import cn.sansotta.market.service.TradeService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class TradeManager(private val tradeDao: TradeDao) : TradeService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun newTrade(trade: Trade) =
            trade.takeIf(Trade.Companion::isValidEntity)
                    ?.let(Trade::toEntity)
                    ?.let { hazard("create trade", false) { tradeDao.insertTrade(it) } } ?: false

    override fun getTradeId(orderId: Long) =
            orderId.takeIf { it > 0L }?.let(tradeDao::selectTradeId)

    override fun getTradeByExtra(extra: String) =
            extra.let(tradeDao::selectTradeByExtra)?.let(::Trade)

    override fun getTradeLocked(orderId: Long) =
            orderId.takeIf { it > 0L }?.let(tradeDao::selectTradeLocked)?.let(::Trade)

    override fun getTradeByTradeIdLocked(tradeId: String) =
            tradeId.takeIf { it.isNotBlank() }?.let(tradeDao::selectTradeByTradeIdLocked)?.let(::Trade)

    override fun voidTrade(orderId: Long): Boolean =
            orderId.takeIf { it > 0L }
                    ?.let { hazard("void trade") { tradeDao.voidTrade(it) } } ?: false

    override fun voidTradeExtra(orderId: Long) =
            orderId.takeIf { it > 0L }
                    ?.let { hazard("void trade extra") { tradeDao.voidTradeExtra(it) } } ?: Unit

    private inline fun <T> hazard(method: String, defaultVal: T, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                defaultVal
            }

    private inline fun <T> hazard(method: String, func: () -> T) =
            try {
                func()
            } catch (ex: RuntimeException) {
                logger.error("Exception when $method", ex)
                null
            }
}
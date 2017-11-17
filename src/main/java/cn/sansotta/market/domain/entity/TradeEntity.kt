package cn.sansotta.market.domain.entity

import java.time.LocalDateTime

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class TradeEntity(
        val oid: Long,
        val method: String,
        val createTime: LocalDateTime,
        val tradeId: String,
        val extra: String?)

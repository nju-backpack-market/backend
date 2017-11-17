package cn.sansotta.market.domain.value

import cn.sansotta.market.domain.ValueObject
import cn.sansotta.market.domain.entity.TradeEntity
import java.time.LocalDateTime

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class Trade(
        var oid: Long,
        var method: String,
        var createTime: LocalDateTime,
        var tradeId: String,
        var extra: String?) : ValueObject<TradeEntity> {
    constructor() : this(-1, "", LocalDateTime.now(), "", "")

    constructor(oid: Long, method: String, tradeId: String, extra: String?)
            : this(oid, method, LocalDateTime.now(), tradeId, extra)

    constructor(po: TradeEntity) : this(po.oid, po.method, po.createTime, po.tradeId, po.extra)

    override fun toEntity() = TradeEntity(oid, method, createTime, tradeId, extra)

    companion object {
        fun isValidEntity(trade: Trade) = trade.oid > 0L &&
                (trade.method == "paypal" || trade.method == "alipay") &&
                trade.tradeId.isNotBlank()
    }
}
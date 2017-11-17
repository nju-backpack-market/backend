package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.TradeEntity;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
public interface TradeDao {
    boolean insertTrade(TradeEntity trade);

    String selectTradeId(long orderId);

    TradeEntity selectTradeByExtra(String extra);

    TradeEntity selectTradeByTradeIdLocked(String tradeId);

    TradeEntity selectTradeLocked(long orderId);

    boolean voidTrade(long orderId);

    void voidTradeExtra(long orderId);
}

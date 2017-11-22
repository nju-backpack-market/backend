package cn.sansotta.market.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.TradeDao;
import cn.sansotta.market.domain.entity.TradeEntity;
import cn.sansotta.market.mapper.TradeMapper;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Repository
public class TradeDaoImpl implements TradeDao {
    private final static Logger logger = LoggerFactory.getLogger(TradeDaoImpl.class);

    private final MybatisUtils.MapperTemplate<TradeMapper> template;

    public TradeDaoImpl(MybatisUtils util) {
        this.template = util.mapperTemplate(TradeMapper.class);
    }

    @Transactional
    @Override
    public boolean insertTrade(TradeEntity trade) {
        return template.exec(trade, TradeMapper::insertTrade) == 1;
    }

    @Override
    public String selectTradeId(long orderId) {
        try {
            return template.exec(orderId, TradeMapper::selectTradeId);
        } catch (RuntimeException ex) {
            logger.error("error when select trade " + ex);
            return null;
        }
    }

    @Override
    public TradeEntity selectTradeByExtra(String extra) {
        try {
            return template.exec(extra, TradeMapper::selectTradeByExtra);
        } catch (RuntimeException ex) {
            logger.error("error when select trade " + ex);
            return null;
        }
    }

    @Override
    public TradeEntity selectTradeByTradeIdLocked(String tradeId) {
        try {
            return template.exec(tradeId, TradeMapper::selectTradeByTradeId);
        } catch (RuntimeException ex) {
            logger.error("error when select trade " + ex);
            return null;
        }
    }

    @Override
    public TradeEntity selectTradeLocked(long orderId) {
        try {
            return template.exec(orderId, TradeMapper::selectTradeLocked);
        } catch (RuntimeException ex) {
            logger.error("error when select trade " + ex);
            return null;
        }
    }

    @Transactional
    @Override
    public boolean voidTrade(long orderId) {
        return template.exec(orderId, TradeMapper::deleteTrade) == 1;
    }

    @Transactional
    @Override
    public void voidTradeExtra(long orderId) {
        template.exec(orderId, TradeMapper::deleteExtra);
    }
}

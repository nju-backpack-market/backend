package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.controller.resource.OrderQuery;
import cn.sansotta.market.dao.OrderDao;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderStatus;
import cn.sansotta.market.mapper.OrderMapper;
import cn.sansotta.market.mapper.ShoppingItemMapper;

/**
 * Created by Hiki on 2017/10/31.
 */
@Component
public class OrderDaoImpl implements OrderDao {
    private final static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private final MybatisUtils.MapperTemplate<OrderMapper> orderTpl;

    private final MybatisUtils.MapperTemplate<ShoppingItemMapper> shoppingItemTpl;

    public OrderDaoImpl(MybatisUtils util) {
        this.orderTpl = util.mapperTemplate(OrderMapper.class);
        this.shoppingItemTpl = util.mapperTemplate(ShoppingItemMapper.class);
    }


    @Override
    public OrderEntity selectOrderById(long id) {
        try {
            return orderTpl.exec(id, OrderMapper::selectOrderById);
        } catch (RuntimeException ex) {
            logger.error("error when select order because of " + ex);
            return null;
        }
    }

    @Override
    public PageInfo<OrderEntity> selectAllOrders(int pageNum) {
        try {
            return orderTpl.paged(pageNum, 30, OrderMapper::selectAllOrders);
        } catch (RuntimeException ex) {
            logger.error("error when select order because of " + ex);
            return null;
        }
    }

    @Override
    public PageInfo<OrderEntity> selectOrdersByQuery(int pageNum, OrderQuery query) {
        try {
            return orderTpl.paged(pageNum, 30, query, OrderMapper::selectOrdersByQuery);
        } catch (RuntimeException ex) {
            logger.error("error when select order because of " + ex);
            return null;
        }
    }

    @Override
    public PageInfo<OrderEntity> selectOrdersByState(int pageNum, OrderStatus state) {
        return orderTpl.paged(pageNum, 30, state, OrderMapper::selectOrdersByState);
    }

    @Transactional
    @Override
    public OrderEntity insertOrder(OrderEntity order) {
        orderTpl.exec(order, OrderMapper::insertOrder);
        shoppingItemTpl.exec(order.getShoppingItems(), ShoppingItemMapper::insertShoppingItems);
        return order;
    }

    @Transactional
    @Override
    public boolean updateOrder(OrderEntity order) {
        int affectedRow = orderTpl.exec(order, OrderMapper::updateOrder);
        return affectedRow > 0;
    }

    @Transactional
    @Override
    public boolean updateOrderStatus(long id, OrderStatus status) {
        int affectedRow = orderTpl.exec(id, status, OrderMapper::updateOrderStatus);
        return affectedRow > 0;
    }
}

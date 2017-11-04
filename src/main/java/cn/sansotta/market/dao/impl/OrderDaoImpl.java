package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
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
            return orderTpl.exec(orderMapper -> orderMapper.selectOrderById(id));
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
    public PageInfo<OrderEntity> selectOrdersByState(int pageNum, OrderStatus state) {
        return orderTpl.paged(pageNum, 30, state, OrderMapper::selectOrdersByState);
    }

    @Transactional
    @Override
    public OrderEntity insertOrder(OrderEntity order) throws RuntimeException {
        orderTpl.exec(orderMapper -> orderMapper.insertOrder(order));
        shoppingItemTpl.exec(shoppingItemMapper -> shoppingItemMapper
                .insertShoppingItems(order.getShoppingItems()));
        return order;
    }

    @Override
    public boolean updateOrder(OrderEntity order) throws RuntimeException {
        int affectedRow = orderTpl.exec(orderMapper -> orderMapper.updateOrder(order));
        return affectedRow > 0;
    }

    @Override
    public boolean updateOrderStatus(long id, OrderStatus status) throws RuntimeException {
        int affectedRow = orderTpl.exec(orderMapper -> orderMapper.updateOrderStatus(id, status));
        return affectedRow > 0;
    }
}

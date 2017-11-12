package cn.sansotta.market.dao;

import com.github.pagehelper.PageInfo;

import cn.sansotta.market.controller.resource.OrderQuery;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderStatus;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface OrderDao {

    OrderEntity selectOrderById(long id);

    OrderEntity selectOrderByIdLocked(long id);

    PageInfo<OrderEntity> selectAllOrders(int pageNum);

    PageInfo<OrderEntity> selectOrdersByQuery(int pageNum, OrderQuery query);

    PageInfo<OrderEntity> selectOrdersByStatus(int pageNum, OrderStatus status);

    OrderEntity insertOrder(OrderEntity order);

    boolean updateOrder(OrderEntity order);

    boolean updateOrderStatus(long id, OrderStatus status);

}

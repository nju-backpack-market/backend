package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderStatus;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface OrderDao {

	OrderEntity selectOrderById(long id);

	PageInfo<OrderEntity> selectAllOrders(int pageNum);

	PageInfo<OrderEntity> selectOrdersByState(int pageNum, OrderStatus state);

	OrderEntity insertOrder(OrderEntity order) throws RuntimeException;

	boolean updateOrder(OrderEntity order) throws RuntimeException;

	boolean updateOrderStatus(long id, OrderStatus status) throws RuntimeException;
}

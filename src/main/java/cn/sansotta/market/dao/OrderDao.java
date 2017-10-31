package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderState;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface OrderDao {

	OrderEntity selectOrderById(long id);

	PageInfo<OrderEntity> selectAllOrder();

	PageInfo<OrderEntity> selectOrderByState(OrderState state);

	void insertOrder(OrderEntity order);

	void updateOrder(OrderEntity order);

	void deleteOrder(long id);

}

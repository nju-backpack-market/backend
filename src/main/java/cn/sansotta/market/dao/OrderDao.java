package cn.sansotta.market.dao;

import cn.sansotta.market.domain.Order;

import java.util.List;

/**
 * Created by Hiki on 2017/10/20.
 */

public interface OrderDao {

	List<Order> selectAll();

	List<Order> selectByStatus();

	Order selectById();

	void insert(Order order);

	void update(Order order);

}

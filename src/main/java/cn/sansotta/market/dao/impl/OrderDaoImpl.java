package cn.sansotta.market.dao.impl;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.OrderDao;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderState;
import cn.sansotta.market.mapper.OrderMapper;
import cn.sansotta.market.mapper.ShoppingItemMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Hiki on 2017/10/31.
 */
@Component
public class OrderDaoImpl implements OrderDao{

	private final MybatisUtils.MapperTemplate<OrderMapper> orderTpl;

	private final MybatisUtils.MapperTemplate<ShoppingItemMapper> shoppingItemTpl;

	public OrderDaoImpl(MybatisUtils util){
		this.orderTpl = util.mapperTemplate(OrderMapper.class);
		this.shoppingItemTpl = util.mapperTemplate(ShoppingItemMapper.class);
	}


	@Override
	public OrderEntity selectOrderById(long id) {
		return orderTpl.exec(orderMapper -> orderMapper.selectOrderById(id));
	}

	@Transactional
	@Override
	public PageInfo<OrderEntity> selectAllOrders(int pageNum) {
		return orderTpl.paged(pageNum, 30, OrderMapper::selectAllOrders);
	}

	@Transactional
	@Override
	public PageInfo<OrderEntity> selectOrdersByState(int pageNum, OrderState state) {
		return orderTpl.paged(pageNum, 30, state, OrderMapper::selectOrdersByState);
	}

	@Transactional
	@Override
	public void insertOrder(OrderEntity order) {
//		orderTpl.exec(orderMapper -> orderMapper.insertOrder(order));
//		shoppingItemTpl.exec(shoppingItemMapper -> shoppingItemMapper.insertShoppingItems(order.getShoppingItems()));
	}

	@Override
	public void updateOrder(OrderEntity order) {
//		orderTpl.exec(orderMapper -> orderMapper.updateOrder(order));
	}

}

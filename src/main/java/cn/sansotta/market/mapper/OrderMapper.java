package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderState;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.ServerEndpoint;
import java.util.List;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface OrderMapper {

	@Select("SELECT * FROM orders WHERE oid=#{id}")
	OrderEntity selectOrderById(long id);

	@Select("SELECT * FROM orders")
	List<OrderEntity> selectAllOrder();

	@Select("SELECT * FROM orders WHERE state=#{state}")
	List<OrderEntity> selectOrderByState(OrderState state);

//	@Insert("INSERT INTO orders(oid, state, time, c_name, c_phone_number, c_address) ")
	void insertOrder(OrderEntity order);

	void updateOrder(OrderEntity order);


	void deleteOrder(long id);

}

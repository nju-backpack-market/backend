package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

import cn.sansotta.market.domain.entity.DeliveryInfoEntity;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderState;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface OrderMapper {

	// TODO TEST
	@Results(id = "orderMap")
	@ConstructorArgs({
            @Arg(id = true, column = "oid", javaType = long.class),
			@Arg(column = "total_price", javaType = double.class),
            @Arg(column = "state", javaType = OrderState.class),
            @Arg(column = "time", javaType = LocalDateTime.class),
            @Arg(resultMap = "cn.sansotta.market.mapper.DummyMapper.deliveryInfoMap",
                    javaType = DeliveryInfoEntity.class),
            @Arg(column = "oid",
					select = "cn.sansotta.market.mapper.ShoppingItemMapper.selectShoppingItemsByOrderId",
					javaType = List.class)})
    @Select("SELECT * FROM orders WHERE oid=#{id}")
    OrderEntity selectOrderById(long id);

	@ResultMap("orderMap")
    @Select("SELECT * FROM orders")
    List<OrderEntity> selectAllOrder();

	@ResultMap("orderMap")
	@Select("SELECT * FROM orders WHERE state=#{state}")
    List<OrderEntity> selectOrderByState(OrderState state);

	// TODO TEST
	@Insert("INSERT INTO orders(total_price, state, time, c_name, c_phone_number, c_email, c_address) " +
            "VALUES (#{totalPrice}, #{state}, #{time}, #{name}, #{phoneNumber}, #{email}, #{address})")
	@Options(useGeneratedKeys = true, keyProperty = "oid")
	void insertOrder(OrderEntity order);

    // TODO TEST
	@Update("UPDATE orders SET " +
			"total_price=#{totalPrice}, state=#{state}, time=#{time}, c_name=#{name}, c_phone_number=#{phoneNumber}, c_email=#{email}, c_address=#{address}" +
			"WHERE oid=#{oid}")
    void updateOrder(OrderEntity order);

	@Delete("DELETE FROM orders WHERE oid=#{id}")
    void deleteOrder(long id);

}

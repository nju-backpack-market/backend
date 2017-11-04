package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

import cn.sansotta.market.domain.entity.DeliveryInfoEntity;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderStatus;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface OrderMapper {

    @Results(id = "orderMap")
    @ConstructorArgs({
            @Arg(id = true, column = "oid", javaType = long.class),
            @Arg(column = "total_price", javaType = Double.class),
            @Arg(column = "state", javaType = OrderStatus.class),
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
    List<OrderEntity> selectAllOrders();


    @ResultMap("orderMap")
    @Select("SELECT * FROM orders WHERE state=#{state}")
    List<OrderEntity> selectOrdersByState(OrderStatus state);


    @Insert("INSERT INTO orders(total_price, state, `time`, c_name, c_phone_number, c_email, c_address) " +
            "VALUES (#{totalPrice}, #{status}, #{time}, #{deliveryInfo.name}, #{deliveryInfo.phoneNumber}, #{deliveryInfo.email}, #{deliveryInfo.address})")
    @Options(useGeneratedKeys = true)
    int insertOrder(OrderEntity order);


    @Update("UPDATE orders SET " +
            "total_price=#{totalPrice}, state=#{status}, c_name=#{deliveryInfo.name}, c_phone_number=#{deliveryInfo.phoneNumber}, c_email=#{deliveryInfo.email}, c_address=#{deliveryInfo.address}" +
            "WHERE oid=#{id}")
    int updateOrder(OrderEntity order);


    @Update("UPDATE orders SET " +
            "state=#{status}" +
            "WHERE oid=#{id}")
    int updateOrderStatus(@Param("id") long id, @Param("status") OrderStatus status);


    @Delete("DELETE FROM orders WHERE oid=#{id}")
    int deleteOrder(long id);

}

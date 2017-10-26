package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

import cn.sansotta.market.domain.entity.BillEntity;
import cn.sansotta.market.domain.entity.DeliveryInfoEntity;
import cn.sansotta.market.domain.entity.OrderEntity;
import cn.sansotta.market.domain.value.OrderState;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface OrderMapper {

    @ConstructorArgs({
            @Arg(id = true, column = "oid", javaType = long.class),
            @Arg(column = "state", javaType = OrderState.class),
            @Arg(column = "time", javaType = LocalDateTime.class),
            @Arg(resultMap = "cn.sansotta.market.mapper.DummyMapper.deliveryInfoMap",
                    javaType = DeliveryInfoEntity.class),
            @Arg(resultMap = "cn.sansotta.market.mapper.DummyMapper.billMap",
                    javaType = BillEntity.class)})
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

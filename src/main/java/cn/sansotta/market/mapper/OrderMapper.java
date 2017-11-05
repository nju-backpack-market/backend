package cn.sansotta.market.mapper;

import cn.sansotta.market.controller.resource.OrderQuery;
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
    List<OrderEntity> selectOrdersByStatus(OrderStatus status);


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

    @ResultMap("orderMap")
    @Select({
            "<script>",
            "SELECT * FROM orders ",
            "WHERE oid IN (SELECT DISTINCT O.oid FROM orders O, shopping_items S ",
            "<where>",
            "<if test= \"id != null\"> O.oid = #{id} </if>",
            "<if test= \"status != null\">AND O.state = #{status} </if>",
            "<if test= \"fromDate != null\">AND <![CDATA[date(O.time) >= #{fromDate}]]> </if>",
            "<if test= \"toDate != null\"> <![CDATA[AND date(O.time) <= #{toDate}]]> </if>",
            "<if test= \"onDate != null\"> AND date(O.time) = #{onDate} </if>",
            "<if test= \"customerName != null\"> AND O.c_name = #{customerName} </if>",
            "<if test= \"phoneNumber != null\"> AND O.c_phone = #{phoneNumber} </if>",
            "<if test= \"productIds != null\"> AND S.pid IN <foreach collection='productIds' item='pid' open='(' close=')' separator=','>#{pid}</foreach> </if>",
            "<if test= \"fromPrice != null\"> <![CDATA[AND O.total_price >= #{fromPrice}]]> </if>",
            "<if test = \"toPrice != null\"> <![CDATA[AND O.total_price <= #{toPrice}]]> </if>",
            "</where>)",
            "</script>"
    })
    List<OrderEntity> selectOrdersByQuery(OrderQuery query);

}

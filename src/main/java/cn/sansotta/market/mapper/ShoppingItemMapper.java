package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import cn.sansotta.market.domain.entity.ShoppingItemEntity;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Mapper
public interface ShoppingItemMapper {

    // TODO TEST
    @Results(id = "shoppingItemMap")
    @ConstructorArgs({
            @Arg(column = "oid", javaType = long.class),
            @Arg(column = "pid", javaType = long.class),
            @Arg(column = "count", javaType = int.class),
            @Arg(column = "unit_price", javaType = double.class),
            @Arg(column = "subtotal_price", javaType = double.class)})
    @Select("SELECT oid, pid, count, unit_price, subtotal_price " +
            "FROM shopping_items WHERE oid = #{oid}")
    List<ShoppingItemEntity> selectShoppingItemsByOrderId(long oid);

    // TODO TEST
    @Insert({
            "<script>",
            "INSERT INTO shopping_items (oid, pid, count, unit_price, subtotal_price) VALUES",
            "<foreach collection='shoppingItems' item='shoppingItem' separator=','>",
            "(#{shoppingItem.oid}, #{shoppingItem.pid}, #{shoppingItem.count}, #{shoppingItem.unitPrice}, #{shoppingItem.subtotalPrice})",
            "</foreach>",
            "</script>"
    })
    void insertShoppingItems(List<ShoppingItemEntity> shoppingItems);

    // TODO TEST
    @Delete("DELETE FROM shopping_items WHERE oid=#{oid}")
    void deleteShoppingItems(long oid);

}

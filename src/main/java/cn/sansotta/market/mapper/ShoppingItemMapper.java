package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import cn.sansotta.market.domain.entity.PriceEntity;
import cn.sansotta.market.domain.entity.ShoppingItemEntity;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Mapper
public interface ShoppingItemMapper {
    @Results(id = "shoppingItemMap")
    @ConstructorArgs({
            @Arg(column = "pid", javaType = long.class),
            @Arg(column = "count", javaType = int.class),
            @Arg(resultMap = "cn.sansotta.market.mapper.DummyMapper.priceMap",
                    javaType = PriceEntity.class),
            @Arg(column = "subtotal_price", javaType = Double.class)})
    @Select("SELECT pid, `count`, origin_price AS origin, actual_price AS actual, subtotal_price " +
            "FROM shopping_items WHERE oid = #{oid}")
    List<ShoppingItemEntity> selectShoppingItemsByOrderId(long oid);
}

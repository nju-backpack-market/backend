package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import cn.sansotta.market.domain.entity.DeliveryInfoEntity;
import cn.sansotta.market.domain.entity.PriceEntity;

/**
 * This class contains mapper for entities not in database actually.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Mapper
public interface DummyMapper {

    @Results(id = "priceMap")
    @ConstructorArgs({
            @Arg(column = "origin", javaType = double.class),
            @Arg(column = "actual", javaType = Double.class)})
    @Select("")
    PriceEntity price();


    @Results(id = "deliveryInfoMap")
    @ConstructorArgs({
            @Arg(column = "c_name", javaType = String.class),
            @Arg(column = "c_phone_number", javaType = String.class),
            @Arg(column = "c_email", javaType = String.class),
            @Arg(column = "c_address", javaType = String.class)})
    @Select("")
    DeliveryInfoEntity deliveryInfoEntity();
}

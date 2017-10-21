package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Hiki on 2017/10/21.
 */
@Mapper
public interface ProductMapper {

	ProductEntity selectProductById();


}

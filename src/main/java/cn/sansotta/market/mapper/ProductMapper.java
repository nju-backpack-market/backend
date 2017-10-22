package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Hiki on 2017/10/21.
 */
@Mapper
public interface ProductMapper {

	ProductEntity selectProductById(long id);

	void insertProduct(ProductEntity product);

	void updateProduct(ProductEntity product);

	void deleteProduct(long id);
}

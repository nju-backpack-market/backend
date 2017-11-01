package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.ProductEntity;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/10/20.
 */
public interface ProductDao {

    ProductEntity selectProductById(long id);

    PageInfo<ProductEntity> selectAllProducts(int pageNum);

    ProductEntity insertProduct(ProductEntity product) throws RuntimeException;

    boolean updateProduct(ProductEntity product) throws RuntimeException;

    boolean deleteProduct(long id) throws RuntimeException;

}

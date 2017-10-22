package cn.sansotta.market.dao;

import com.github.pagehelper.PageInfo;

import cn.sansotta.market.domain.entity.ProductEntity;

/**
 * Created by Hiki on 2017/10/20.
 */
public interface ProductDao {
    ProductEntity selectProductById(long id);

    PageInfo<ProductEntity> selectAllProducts(int pageNum, int pageSize);
}

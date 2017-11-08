package cn.sansotta.market.dao;

import com.github.pagehelper.PageInfo;

import java.util.List;

import cn.sansotta.market.domain.entity.ProductEntity;

/**
 * Created by Hiki on 2017/10/20.
 */
public interface ProductDao {

    ProductEntity selectProductById(long id);

    PageInfo<ProductEntity> selectAllProducts(int pageNum);

    ProductEntity insertProduct(ProductEntity product);

    List<ProductEntity> insertProducts(List<ProductEntity> products);

    boolean updateProduct(ProductEntity product) throws RuntimeException;

    boolean deleteProduct(long id) throws RuntimeException;

    boolean deleteProductImage(String imageName);
}

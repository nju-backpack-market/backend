package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.ProductDao;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.mapper.ProductMapper;

/**
 * Created by Hiki on 2017/10/21.
 */
@Component
public class ProductDaoImpl implements ProductDao {

    private final MybatisUtils.MapperTemplate<ProductMapper> productTpl;

    public ProductDaoImpl(MybatisUtils util) {
        this.productTpl = util.mapperTemplate(ProductMapper.class);
    }

    @Override
    public ProductEntity selectProductById(long id) {
        return productTpl.exec(productMapper -> productMapper.selectProductById(id));
    }

    @Transactional
    @Override
    public PageInfo<ProductEntity> selectAllProducts(int pageNum) {
        return productTpl.paged(pageNum, 30, ProductMapper::selectAllProducts);
    }

    @Override
    public void insertProduct(ProductEntity product) {
//        productTpl.exec(productMapper -> productMapper.insertProduct(product));
    }

    @Override
    public void updateProduct(ProductEntity product) {
//        productTpl.exec(productMapper -> productMapper.updateProduct(product));
    }

    @Override
    public void deleteProduct(long id) {
//        productTpl.exec(productMapper -> productMapper.deleteProduct(id));
    }
}
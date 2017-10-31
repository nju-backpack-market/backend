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
    // e.g. use
    private final MybatisUtils util;
    // or use
    private final MybatisUtils.MapperTemplate<ProductMapper> template;

    public ProductDaoImpl(MybatisUtils util) {
        this.util = util;
        // or
        this.template = util.mapperTemplate(ProductMapper.class);
    }

    @Override
    public ProductEntity selectProductById(long id) {
        // example:
        ProductEntity product0 =
                util.withMapper(ProductMapper.class, mapper -> mapper.selectProductById(id));
        // if you think it not so cool, try
        ProductEntity product1 = template.exec(mapper -> mapper.selectProductById(id));
        ProductEntity product2 = template.exec(id, ProductMapper::selectProductById);
        return product0;
    }

    @Transactional
    @Override
    public PageInfo<ProductEntity> selectAllProducts(int pageNum) {
        // example:
        return template.paged(pageNum, 30, ProductMapper::selectAllProduct);
    }

    @Override
    public void insertProduct(ProductEntity product) {

    }

    @Override
    public void updateProduct(ProductEntity product) {

    }

    @Override
    public void deleteProduct(long id) {

    }
}
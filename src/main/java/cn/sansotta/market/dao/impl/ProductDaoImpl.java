package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.ProductDao;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.mapper.ProductMapper;

import java.util.List;

/**
 * Created by Hiki on 2017/10/21.
 */
@Component
public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final MybatisUtils.MapperTemplate<ProductMapper> productTpl;

    public ProductDaoImpl(MybatisUtils util) {
        this.productTpl = util.mapperTemplate(ProductMapper.class);
    }

    @Override
    public ProductEntity selectProductById(long id) {
        try {
            return productTpl.exec(productMapper -> productMapper.selectProductById(id));
        } catch (RuntimeException ex) {
            logger.error("error when select product because of " + ex);
            return null;
        }
    }

    @Override
    public PageInfo<ProductEntity> selectAllProducts(int pageNum) {
        try {
            return productTpl.paged(pageNum, 30, ProductMapper::selectAllProducts);
        } catch (RuntimeException ex) {
            logger.error("error when select product because of " + ex);
            return null;
        }
    }

    @Transactional
    @Override
    public ProductEntity insertProduct(ProductEntity product) {
        productTpl.exec(productMapper -> productMapper.insertProduct(product));
        return product;
    }

    @Transactional
	@Override
	public List<ProductEntity> insertProducts(List<ProductEntity> products) throws RuntimeException{
    	productTpl.exec(productMapper -> productMapper.insertProducts(products));
    	return products;
	}

	@Transactional
    @Override
    public boolean updateProduct(ProductEntity product) {
        int affectedRow = productTpl.exec(productMapper -> productMapper.updateProduct(product));
        return affectedRow > 0;
    }


    @Transactional
    @Override
    public boolean deleteProduct(long id) {
        int affectedRow = productTpl.exec(productMapper -> productMapper.deleteProduct(id));
        return affectedRow > 0;
    }
}
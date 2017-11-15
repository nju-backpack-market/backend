package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.ProductDao;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.mapper.ProductImageMapper;
import cn.sansotta.market.mapper.ProductMapper;

/**
 * Created by Hiki on 2017/10/21.
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final MybatisUtils.MapperTemplate<ProductMapper> productTpl;

    private final MybatisUtils.MapperTemplate<ProductImageMapper> productImageTpl;

    public ProductDaoImpl(MybatisUtils util) {
        this.productTpl = util.mapperTemplate(ProductMapper.class);
        this.productImageTpl = util.mapperTemplate(ProductImageMapper.class);
    }

    @Override
    public ProductEntity selectProductById(long id, boolean cascade) {
        try {
            return productTpl.exec(id,
                    cascade ? ProductMapper::selectProductById :
                            ProductMapper::selectProductByIdNoPictures);
        } catch (RuntimeException ex) {
            logger.error("error when select product ", ex);
            return null;
        }
    }

    @Override
    public PageInfo<ProductEntity> selectProductByName(String name, int page, boolean onlyOnSale) {
        try {
            return productTpl.paged(page, 30, "%" + name + '%',
                    onlyOnSale ? ProductMapper::selectProductByNameOnSale :
                            ProductMapper::selectProductByName);
        } catch (RuntimeException ex) {
            logger.error("error when select product ", ex);
            return null;
        }
    }

    @Override
    public String selectProductName(long id) {
        try {
            return productTpl.exec(id, ProductMapper::selectProductName);
        } catch (RuntimeException ex) {
            logger.error("error when select product ", ex);
            return null;
        }
    }

    @Override
    public PageInfo<ProductEntity> selectAllProducts(int pageNum, boolean onlyOnSale) {
        try {
            return productTpl.paged(pageNum, 30,
                    onlyOnSale ? ProductMapper::selectAllProductsOnSale :
                            ProductMapper::selectAllProducts);
        } catch (RuntimeException ex) {
            logger.error("error when select product because of " + ex);
            return null;
        }
    }

    @Transactional
    @Override
    public ProductEntity insertProduct(ProductEntity product) {
        productTpl.exec(product, ProductMapper::insertProduct);
        if(!product.getImages().isEmpty())
            productImageTpl.exec(product.getImages(), product.getId(),
                    ProductImageMapper::insertProductImages);
        return product;
    }

    @Transactional
    @Override
    public List<ProductEntity> insertProducts(List<ProductEntity> products) throws RuntimeException {
        productTpl.exec(products, ProductMapper::insertProducts);
        for (ProductEntity product : products)
            if(!product.getImages().isEmpty())
                productImageTpl.exec(product.getImages(), product.getId(),
                        ProductImageMapper::insertProductImages);
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
        int affectedRow = productTpl.exec(id, ProductMapper::deleteProduct);
        return affectedRow > 0;
    }

    @Override
    public boolean deleteProductImage(String imageName) {
        int affectedRow = productImageTpl
                .exec(productImageMapper -> productImageMapper.deleteProductImage(imageName));
        return affectedRow > 0;
    }

}
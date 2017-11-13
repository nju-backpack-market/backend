package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import cn.sansotta.market.domain.entity.ProductEntity;

/**
 * Created by Hiki on 2017/10/21.
 */
@Mapper
public interface ProductMapper {

    @Results(id = "productMap")
    @ConstructorArgs({
            @Arg(id = true, column = "pid", javaType = long.class),
            @Arg(column = "pname", javaType = String.class),
            @Arg(column = "price", javaType = double.class),
            @Arg(column = "description", javaType = String.class),
            @Arg(column = "pid",
                    select = "cn.sansotta.market.mapper.ProductMapper.selectProductImageByProductId",
                    javaType = List.class),
            @Arg(column = "on_sale", javaType = boolean.class)
    })
    @Select("SELECT * FROM products WHERE pid=#{id}")
    ProductEntity selectProductById(long id);

    @Results(id = "productMapNoPictures")
    @ConstructorArgs({
            @Arg(id = true, column = "pid", javaType = long.class),
            @Arg(column = "pname", javaType = String.class),
            @Arg(column = "price", javaType = double.class),
            @Arg(column = "description", javaType = String.class),
            @Arg(column = "on_sale", javaType = boolean.class)
    })
    @Select("SELECT * FROM products WHERE pid=#{id}")
    ProductEntity selectProductByIdNoPictures(long id);

    @ResultMap("productMap")
    @Select("SELECT * FROM products WHERE pname LIKE #{name}")
    List<ProductEntity> selectProductByName(@Param("name") String name);

    @ResultMap("productMap")
    @Select("SELECT * FROM products WHERE pname LIKE #{name} AND on_sale = TRUE")
    List<ProductEntity> selectProductByNameOnSale(@Param("name") String name);

    @ResultMap("productMap")
    @Select("SELECT * FROM products")
    List<ProductEntity> selectAllProducts();

    @ResultMap("productMap")
    @Select("SELECT * FROM products WHERE on_sale = TRUE")
    List<ProductEntity> selectAllProductsOnSale();

    @Options(useGeneratedKeys = true, keyColumn = "pid")
    @Insert("INSERT INTO products(pname, price, description, on_sale)" +
            "VALUES (#{name}, #{price}, #{description}, #{onSale})")
    int insertProduct(ProductEntity product);

    @Options(useGeneratedKeys = true, keyColumn = "pid")
    @Insert({
            "<script>",
            "INSERT INTO products(pname, price, description) VALUES",
            "<foreach collection='list' item='product' separator=','>",
            "(#{product.name}, #{product.price}, #{product.description})",
            "</foreach>",
            "</script>"
    })
    int insertProducts(List<ProductEntity> products);

    @Update("UPDATE products SET " +
            "pname=#{name}, price=#{price}, description=#{description}, on_sale=#{onSale} " +
            "WHERE pid=#{id}")
    int updateProduct(ProductEntity product);

    @Delete("DELETE FROM products WHERE pid=#{id}")
    int deleteProduct(@Param("id") long id);

    @Select("SELECT * FROM product_images WHERE pid = #{pid}")
    List<String> selectProductImageByProductId(@Param("pid") long pid);
}

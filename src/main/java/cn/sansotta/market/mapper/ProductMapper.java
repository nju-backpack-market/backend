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
            @Arg(column = "description", javaType = String.class)})
    @Select("SELECT * FROM products WHERE pid=#{id}")
    ProductEntity selectProductById(@Param("id") long id);

    @ResultMap("productMap")
    @Select("SELECT * FROM products")
    List<ProductEntity> selectAllProduct();

    @Insert("INSERT INTO products(pname, price, description)" +
            "VALUES (#{name}, #{price}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "pid")
    void insertProduct(ProductEntity product);

    @Update("UPDATE products SET " +
            "pname=#{name}, price = #{price}, description = #{description} " +
            "WHERE pid = #{id}")
    void updateProduct(ProductEntity product);

    @Delete("DELETE FROM products WHERE pid = #{id}")
    void deleteProduct(@Param("id") long id);
}

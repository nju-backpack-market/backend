package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Hiki on 2017/10/21.
 */
@Mapper
public interface ProductMapper {
    @Results(id = "productMap")
    @ConstructorArgs({
            @Arg(id = true, column = "pid"),
            @Arg(column = "pname"),
            @Arg(column = "price"),
            @Arg(column = "description")})
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

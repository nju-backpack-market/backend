package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.*;

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
			@Arg(column = "pid", select = "cn.sansotta.market.mapper.ProductMapper.selectProductImageByProductId",
				javaType = List.class)
			})
    @Select("SELECT * FROM products WHERE pid=#{id}")
    ProductEntity selectProductById(long id);


    @ResultMap("productMap")
    @Select("SELECT * FROM products")
    List<ProductEntity> selectAllProducts();


	@Options(useGeneratedKeys = true, keyColumn = "pid")
	@Insert("INSERT INTO products(pname, price, description)" +
            "VALUES (#{name}, #{price}, #{description})")
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
	@SelectKey(statement="SELECT LAST_INSERT_ID();", resultType = long.class, before = false, keyProperty = "id")
	int insertProducts(List<ProductEntity> products);

    @Update("UPDATE products SET " +
            "pname=#{name}, price=#{price}, description=#{description} " +
            "WHERE pid=#{id}")
    int updateProduct(ProductEntity product);


    @Delete("DELETE FROM products WHERE pid=#{id}")
	int deleteProduct(long id);




}

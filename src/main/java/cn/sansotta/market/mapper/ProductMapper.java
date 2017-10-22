package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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
            @Arg(id = true, column = "pid"),
            @Arg(column = "pname"),
            @Arg(column = "price"),
            @Arg(column = "description")})
    @Select("SELECT * FROM product WHERE id=#{id}")
    ProductEntity selectProductById(@Param("id") long id);

    @ResultMap("productMap")
    @Select("SELECT * FROM product")
    List<ProductEntity> selectAllProduct();

    @Insert("INSERT INTO product(pid, pname, price, description)" +
            "VALUES (#{id}, #{name}, #{price}, #{description})")
    void insertProduct(ProductEntity product);

    @Update("UPDATE product SET " +
            "pname=#{name}, price = #{price}, description = #{description} " +
            "WHERE pid = #{id}")
    void updateProduct(ProductEntity product);

    @Delete("DELETE FROM product WHERE pid = #{id}")
    void deleteProduct(long id);
}

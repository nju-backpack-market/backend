package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Hiki
 */
@Mapper
public interface ProductImageMapper {

    @Results(id = "productImageMap")
    @ConstructorArgs({
            @Arg(column = "image_name", javaType = String.class)
    })
    @Select("SELECT image_name FROM product_images WHERE pid=#{pid} ORDER BY number")
    List<String> selectProductImageByProductId(long pid);

    @Insert({
            "<script>",
            "<if test=\"_databaseId == 'mysql'\">",
            "INSERT INTO product_images (image_name, pid, number) VALUES ",
            "<foreach collection='list' item='image' separator=',' index='idx'>",
            "(#{pid}, #{image}, #{idx})",
            "</foreach>",
            "ON DUPLICATE KEY UPDATE number=VALUES(NUMBER)",
            "</if>",
            "<if test=\"_databaseId == 'h2'\">",
            "MERGE INTO product_images KEY(pid, image_name) VALUES ",
            "<foreach collection='list' item='image' separator=',' index='idx'>",
            "(#{pid}, #{image}, #{idx})",
            "</foreach>",
            "</if>",
            "</script>"
    })
    int insertProductImages(@Param("list") List<String> images, @Param("pid") long pid);

    @Delete("DELETE FROM product_images WHERE pid=#{pid} AND `image_name`=#{name}")
    int deleteProductImage(@Param("pid") long pid, @Param("name") String name);
}

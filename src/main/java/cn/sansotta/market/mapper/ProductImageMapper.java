package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Hiki
 * @create 2017-11-08 20:33
 */
public interface ProductImageMapper {

	@Results(id = "prodcutImageMap")
	@ConstructorArgs({
			@Arg(column = "image_name", javaType = String.class)
	})
	@Select("SELECT image_name FROM product_images WHERE pid=#{pid}")
	List<String> selectProductImageByProductId(long pid);

	@Insert({
			"<script>",
			"INSERT INTO product_images (image_name, pid) VALUES",
			"<foreach collection='images' item='image' separator=','>",
			"(#{image}, #{pid})",
			"</foreach>",
			"</script>"
	})
	int insertProductImages(@Param("images") List<String> images, @Param("pid") long pid);

	@Delete("DELETE FROM product_images WHERE image_name=#{image}")
	int deleteProductImage(@Param("image") String image);

	@Delete("DELETE FROM product_images WHERE pid=#{pid}")
	int deleteProductImageByProductId(@Param("pid") long pid);
}

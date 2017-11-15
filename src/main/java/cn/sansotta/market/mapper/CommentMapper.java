package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.CommentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Hiki
 * @create 2017-11-13 21:54
 */
public interface CommentMapper {

	@Results(id = "commentMap")
	@ConstructorArgs({
			@Arg(id = true, column = "oid", javaType = long.class),
			@Arg(id = true, column = "pid", javaType = long.class),
			@Arg(column = "rating", javaType = double.class),
			@Arg(column = "content", javaType = String.class)
	})
	@Select("SELECT * FROM comments WHERE oid=#{oid}, pid=#{pid}")
	CommentEntity selectCommentsByOrderIdAndProductId(long oid, long pid);

	@ResultMap("commentMap")
	@Select("SELECT * FROM comments WHERE pid=#{pid}")
	List<CommentEntity> selectCommentsByProductId(long pid);

	@ResultMap("commentMap")
	@Select("SELECT * FROM comments WHERE oid=#{oid}")
	List<CommentEntity> selectCommentsByOrderId(long oid);


	@Insert("INSERT INTO comments(oid, pid, rating, content) VALUES (#{oid}, #{pid}, #{rating}, #{content})")
	int insertComment(CommentEntity comment);

	@Insert({
			"<script>",
			"INSERT INTO comments(oid, pid, rating, content) VALUES",
			"<foreach collection='comments' item='comment' separator=','>",
			"(#{comment.oid}, #{comment.pid}, #{comment.rating}, #{comment.content})",
			"</foreach>",
			"</script>"
	})
	int insertComments(List<CommentEntity> comments);

}

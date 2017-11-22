package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import cn.sansotta.market.domain.entity.CommentEntity;

/**
 * @author Hiki
 */
public interface CommentMapper {

    @Results(id = "commentMap")
    @ConstructorArgs({
            @Arg(id = true, column = "pid", javaType = long.class),
            @Arg(id = true, column = "oid", javaType = long.class),
            @Arg(column = "rating", javaType = int.class),
            @Arg(column = "content", javaType = String.class)
    })
    @Select("SELECT * FROM comments WHERE oid=#{oid} AND pid=#{pid}")
    CommentEntity selectCommentsByOrderIdAndProductId(@Param("pid") long pid, @Param("oid") long oid);

    @ResultMap("commentMap")
    @Select("SELECT * FROM comments WHERE pid=#{pid}")
    List<CommentEntity> selectCommentsByProductId(@Param("pid") long pid);

    @ResultMap("commentMap")
    @Select("SELECT * FROM comments WHERE oid=#{oid}")
    List<CommentEntity> selectCommentsByOrderId(@Param("oid") long oid);

    @Insert("INSERT INTO comments(oid, pid, rating, content) VALUES (#{oid}, #{pid}, #{rating}, #{content})")
    int insertComment(CommentEntity comment);

    @Insert({
            "<script>",
            "INSERT INTO comments(oid, pid, rating, content) VALUES",
            "<foreach collection='list' item='comment' separator=','>",
            "(#{comment.oid}, #{comment.pid}, #{comment.rating}, #{comment.content})",
            "</foreach>",
            "</script>"
    })
    int insertComments(@Param("list") List<CommentEntity> comments);
}

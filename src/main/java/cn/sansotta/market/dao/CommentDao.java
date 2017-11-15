package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.CommentEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Hiki
 * @create 2017-11-13 22:02
 */
public interface CommentDao {

	CommentEntity selectCommentsByOrderIdAndProductId(long oid, long pid);

	PageInfo<CommentEntity> selectCommentsByProductId(int pageNum, long pid);

	PageInfo<CommentEntity> selectCommentsByOrderId(int pageNum, long oid);

	CommentEntity insertComment(CommentEntity comment);

	List<CommentEntity> insertComments(List<CommentEntity> comments);

}

package cn.sansotta.market.dao;

import com.github.pagehelper.PageInfo;

import java.util.List;

import cn.sansotta.market.domain.entity.CommentEntity;

/**
 * @author Hiki
 */
public interface CommentDao {

    CommentEntity selectCommentsByOrderIdAndProductId(long oid, long pid);

    PageInfo<CommentEntity> selectCommentsByProductId(int pageNum, long pid);

    PageInfo<CommentEntity> selectCommentsByOrderId(int pageNum, long oid);

    boolean insertComment(CommentEntity comment);

    boolean insertComments(List<CommentEntity> comments);
}

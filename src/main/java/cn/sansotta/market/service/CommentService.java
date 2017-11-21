package cn.sansotta.market.service;

import cn.sansotta.market.domain.entity.CommentEntity;
import cn.sansotta.market.domain.value.Comment;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author Hiki
 * @create 2017-11-13 22:11
 */
public interface CommentService {

	Comment getCommentByOrderIdAndProductId(long oid, long pid);

	PageInfo<Comment> getCommentsByProductId(int pageNum, long pid);

	List<Comment> addComments(List<Comment> comments);

	Comment addComment(Comment comment);

}

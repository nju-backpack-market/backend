package cn.sansotta.market.service;

import com.github.pagehelper.PageInfo;

import java.util.List;

import cn.sansotta.market.domain.value.Comment;

/**
 * @author Hiki
 */
public interface CommentService {
    Comment comment(long pid, long oid);

    PageInfo<Comment> commentsOfProduct(long pid, int pageNum);

    List<Comment> addComments(List<Comment> comments);
}

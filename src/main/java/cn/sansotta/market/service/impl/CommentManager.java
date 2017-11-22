package cn.sansotta.market.service.impl;

import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import cn.sansotta.market.common.Utils;
import cn.sansotta.market.dao.CommentDao;
import cn.sansotta.market.domain.value.Comment;
import cn.sansotta.market.service.CommentService;

/**
 * @author Hiki
 */
@Service
public class CommentManager implements CommentService {
    private final CommentDao commentDao;

    public CommentManager(CommentDao commentDao) {this.commentDao = commentDao;}

    @Override
    public Comment comment(long pid, long oid) {
        if(oid <= 0L || pid <= 0L) return null;

        return new Comment(commentDao.selectCommentsByOrderIdAndProductId(oid, pid));
    }

    @Override
    public PageInfo<Comment> commentsOfProduct(long pid, int pageNum) {
        if(pageNum < 0 || pid < 0L) return null;
        return Utils.copyPageInfo(commentDao.selectCommentsByProductId(pageNum, pid), Comment::new);
    }

    @Override
    public List<Comment> addComments(List<Comment> comments) {
        List<Comment> valid =
                comments.stream().filter(Comment::isValidEntity).collect(Collectors.toList());
        return commentDao
                .insertComments(valid.stream().map(Comment::toEntity).collect(Collectors.toList())) ?
                valid : null;
    }
}

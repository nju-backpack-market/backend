package cn.sansotta.market.service.impl;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.common.Utils;
import cn.sansotta.market.dao.CommentDao;
import cn.sansotta.market.dao.impl.CommentDaoImpl;
import cn.sansotta.market.domain.entity.CommentEntity;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.domain.value.Comment;
import cn.sansotta.market.domain.value.Product;
import cn.sansotta.market.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hiki
 * @create 2017-11-21 17:02
 */
@Service
public class CommentManager implements CommentService {

	@Autowired
	private CommentDao commentDao;

	@Override
	public Comment getCommentByOrderIdAndProductId(long oid, long pid) {
		return new Comment(commentDao.selectCommentsByOrderIdAndProductId(oid, pid));
	}

	@Override
	public PageInfo<Comment> getCommentsByProductId(int pageNum, long pid) {
		PageInfo<CommentEntity> commentEntities = commentDao.selectCommentsByProductId(pageNum, pid);
		return Utils.copyPageInfo(commentEntities, e -> new Comment(e));
	}

	@Override
	public List<Comment> addComments(List<Comment> comments) {
		List<CommentEntity> entities = comments.stream().map(e -> (CommentEntity)e.toEntity()).collect(Collectors.toList());
		return commentDao.insertComments(entities).stream().map(e -> new Comment(e)).collect(Collectors.toList());
	}

	@Override
	public Comment addComment(Comment comment) {
		CommentEntity entity = (CommentEntity) comment.toEntity();
		return new Comment(commentDao.insertComment(entity));
	}
}

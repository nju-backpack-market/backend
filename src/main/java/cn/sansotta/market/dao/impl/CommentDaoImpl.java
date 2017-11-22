package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.CommentDao;
import cn.sansotta.market.domain.entity.CommentEntity;
import cn.sansotta.market.mapper.CommentMapper;

/**
 * @author Hiki
 */
@Repository
public class CommentDaoImpl implements CommentDao {

    private final static Logger logger = LoggerFactory.getLogger(CommentDaoImpl.class);

    private final MybatisUtils.MapperTemplate<CommentMapper> commentTpl;

    public CommentDaoImpl(MybatisUtils util) {
        this.commentTpl = util.mapperTemplate(CommentMapper.class);
    }

    @Override
    public CommentEntity selectCommentsByOrderIdAndProductId(long oid, long pid) {
        try {
            return commentTpl.exec(pid, oid, CommentMapper::selectCommentsByOrderIdAndProductId);
        } catch (RuntimeException ex) {
            logger.error("error when select comment because of ", ex);
            return null;
        }
    }

    @Override
    public PageInfo<CommentEntity> selectCommentsByProductId(int pageNum, long pid) {
        try {
            return commentTpl.paged(pid, CommentMapper::selectCommentsByProductId);
        } catch (RuntimeException ex) {
            logger.error("error when select comment because of ", ex);
            return null;
        }
    }

    @Override
    public PageInfo<CommentEntity> selectCommentsByOrderId(int pageNum, long oid) {
        try {
            return commentTpl.paged(oid, CommentMapper::selectCommentsByOrderId);
        } catch (RuntimeException ex) {
            logger.error("error when select comment because of ", ex);
            return null;
        }
    }

    @Override
    public boolean insertComment(CommentEntity comment) {
        return 0 != commentTpl.exec(comment, CommentMapper::insertComment);
    }

    @Override
    public boolean insertComments(List<CommentEntity> comments) {
        return 0 != commentTpl.exec(comments, CommentMapper::insertComments);
    }
}

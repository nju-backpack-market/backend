package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.UserDao;
import cn.sansotta.market.domain.entity.UserEntity;
import cn.sansotta.market.mapper.UserMapper;

/**
 * Created by Hiki on 2017/11/1.
 */
public class UserDaoImpl implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final MybatisUtils.MapperTemplate<UserMapper> userTpl;

    public UserDaoImpl(MybatisUtils util) {
        this.userTpl = util.mapperTemplate(UserMapper.class);
    }

    @Override
    public UserEntity selectUserByUsername(String username) {
        try {
            return userTpl.exec(username, UserMapper::selectUserByUsername);
        } catch (RuntimeException ex) {
            logger.error("error when select user because of " + ex);
            return null;
        }
    }

    @Override
    public PageInfo<UserEntity> selectAllUsers(int pageNum) {
        try {
            return userTpl.paged(pageNum, 30, UserMapper::selectAllUsers);
        } catch (RuntimeException ex) {
            logger.error("error when select user because of " + ex);
            return null;
        }
    }

    @Transactional
    @Override
    public UserEntity insertUser(UserEntity user) {
        userTpl.exec(user, UserMapper::insertUser);
        return user;
    }

    @Transactional
    @Override
    public boolean updateUser(UserEntity user) {
        int affectedRow = userTpl.exec(user, UserMapper::updateUser);
        return affectedRow > 0;
    }

    @Transactional
    @Override
    public boolean deleteUser(String username) {
        int affectedRow = userTpl.exec(username, UserMapper::deleteUser);
        return affectedRow > 0;
    }
}

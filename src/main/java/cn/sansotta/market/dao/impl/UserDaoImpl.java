package cn.sansotta.market.dao.impl;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.UserDao;
import cn.sansotta.market.domain.entity.UserEntity;
import cn.sansotta.market.mapper.ProductMapper;
import cn.sansotta.market.mapper.UserMapper;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/11/1.
 */
public class UserDaoImpl implements UserDao{

	private final MybatisUtils.MapperTemplate<UserMapper> userTpl;

	public UserDaoImpl(MybatisUtils util) {
		this.userTpl = util.mapperTemplate(UserMapper.class);
	}

	@Override
	public UserEntity selectUserByUsername(String username) {
		return userTpl.exec(userMapper -> userMapper.selectUserByUsername(username));
	}

	@Override
	public PageInfo<UserEntity> selectAllUsers(int pageNum) {
		return userTpl.paged(pageNum, 30, UserMapper::selectAllUsers);
	}

	@Override
	public void insertUser(UserEntity user) {
//		userTpl.exec(userMapper -> userMapper.insertUser(user));
	}

	@Override
	public void updateUser(UserEntity user) {
//		userTpl.exec(userMapper -> userMapper.updateUser(user));
	}

	@Override
	public void deleteUser(String username) {
//		userTpl.exec(userMapper -> userMapper.deleteUser(username));
	}
}

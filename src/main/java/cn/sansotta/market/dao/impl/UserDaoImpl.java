package cn.sansotta.market.dao.impl;

import com.github.pagehelper.PageInfo;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.dao.UserDao;
import cn.sansotta.market.domain.entity.UserEntity;
import cn.sansotta.market.mapper.UserMapper;

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
	public UserEntity insertUser(UserEntity user) throws RuntimeException{
		try {
			userTpl.exec(userMapper -> userMapper.insertUser(user));
			return user;
		} catch (RuntimeException e){
			throw e;
		}
	}

	@Override
	public boolean updateUser(UserEntity user) throws RuntimeException{
		try {
			int affectedRow = userTpl.exec(userMapper -> userMapper.updateUser(user));
			return affectedRow > 0;
		} catch (RuntimeException e){
			throw e;
		}
	}

	@Override
	public boolean deleteUser(String username) throws RuntimeException{
		try {
			int affectedRow = userTpl.exec(userMapper -> userMapper.deleteUser(username));
			return affectedRow > 0;
		} catch (RuntimeException e){
			throw e;
		}
	}
}

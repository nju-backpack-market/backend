package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.UserEntity;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface UserDao {

	UserEntity selectUserByUsername(String username);

	void insertUser(UserEntity user);

	void updateUser(UserEntity user);

	void deleteUser(String username);

}

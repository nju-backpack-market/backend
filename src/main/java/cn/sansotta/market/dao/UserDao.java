package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.UserEntity;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface UserDao {

	UserEntity selectUserByUsername(String username);

	PageInfo<UserEntity> selectAllUsers(int pageNum);

	void insertUser(UserEntity user);

	void updateUser(UserEntity user);

	void deleteUser(String username);

}

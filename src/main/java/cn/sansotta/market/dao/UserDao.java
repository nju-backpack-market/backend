package cn.sansotta.market.dao;

import cn.sansotta.market.domain.entity.UserEntity;
import com.github.pagehelper.PageInfo;

/**
 * Created by Hiki on 2017/10/31.
 */
public interface UserDao {

	UserEntity selectUserByUsername(String username);

	PageInfo<UserEntity> selectAllUsers(int pageNum);

	UserEntity insertUser(UserEntity user) throws RuntimeException;

	boolean updateUser(UserEntity user) throws RuntimeException;

	boolean deleteUser(String username) throws RuntimeException;

}

package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface UserMapper {

	@Select("SELECT * from users WHERE username=#{username}")
	UserEntity selectUserByUsername(String username);

	@Insert("INSERT INTO users(username, password) VALUES (#{username}, #{password})")
	void insertUser(UserEntity user);

	@Update("UPDATE users SET password=#{password} WHERE username=#{username}")
	void updateUser(UserEntity user);

	@Delete("DELETE FROM users WHERE username=#{username}")
	void deleteUser(String username);

}

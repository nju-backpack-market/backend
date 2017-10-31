package cn.sansotta.market.mapper;

import cn.sansotta.market.domain.entity.UserEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Created by Hiki on 2017/10/26.
 */
@Mapper
public interface UserMapper {

	@Select("SELECT * FROM users WHERE username=#{username}")
	UserEntity selectUserByUsername(String username);


	@Select("SELECT * FROM users")
	List<UserEntity> selectAllUsers();


	@Insert("INSERT INTO users(username, password) VALUES (#{username}, #{password})")
	void insertUser(UserEntity user);


	@Update("UPDATE users SET password=#{password} WHERE username=#{username}")
	void updateUser(UserEntity user);


	@Delete("DELETE FROM users WHERE username=#{username}")
	void deleteUser(String username);

}

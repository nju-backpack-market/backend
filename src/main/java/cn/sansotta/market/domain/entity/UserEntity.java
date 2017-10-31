package cn.sansotta.market.domain.entity;

/**
 * Created by Hiki on 2017/10/26.
 */
public class UserEntity {

	private String username;

	private String password;

	public UserEntity(){}

	public UserEntity(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

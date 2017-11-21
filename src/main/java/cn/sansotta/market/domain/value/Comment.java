package cn.sansotta.market.domain.value;

import cn.sansotta.market.domain.ValueObject;
import cn.sansotta.market.domain.entity.CommentEntity;

/**
 * @author Hiki
 * @create 2017-11-21 16:49
 */
public class Comment implements ValueObject {

	private long oid;

	private long pid;

	private double rating;

	private String content;

	public Comment(long oid, long pid, double rating, String content) {
		this.oid = oid;
		this.pid = pid;
		this.rating = rating;
		this.content = content;
	}

	public Comment(CommentEntity entity) {
		this.oid = entity.getOid();
		this.pid = entity.getPid();
		this.rating = entity.getRating();
		this.content = entity.getContent();
	}

	@Override
	public Object toEntity() {
		return new CommentEntity(oid, pid, rating, content);
	}



}

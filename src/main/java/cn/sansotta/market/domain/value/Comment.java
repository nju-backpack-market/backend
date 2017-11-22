package cn.sansotta.market.domain.value;

import java.util.Objects;

import cn.sansotta.market.domain.ValueObject;
import cn.sansotta.market.domain.entity.CommentEntity;

/**
 * @author Hiki
 */
public class Comment implements ValueObject<CommentEntity> {

    private long pid;
    private long oid;
    private int rating;
    private String content;

    public Comment() {
        this.oid = -1L;
        this.pid = -1L;
        this.rating = -1;
        this.content = "";
    }

    public Comment(long pid, long oid, int rating, String content) {
        this.pid = pid;
        this.oid = oid;
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
    public CommentEntity toEntity() {
        return new CommentEntity(oid, pid, rating, content);
    }

    public static boolean isValidEntity(Comment comment) {
        return comment.oid > 0L && comment.pid > 0L && comment.rating >= 1 && comment.rating <= 5 &&
                comment.content != null;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return pid == comment.pid &&
                oid == comment.oid &&
                rating == comment.rating &&
                Objects.equals(content, comment.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, oid, rating, content);
    }
}

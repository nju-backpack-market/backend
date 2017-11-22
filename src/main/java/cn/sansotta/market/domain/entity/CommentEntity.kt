package cn.sansotta.market.domain.entity

/**
 * Po for comments
 *
 * Table: comments(oid, pid, rating, content)
 *
 *
 * @author Hiki
 * @create 2017-11-13 20:26
 */
data class CommentEntity(
        val pid: Long,
        val oid: Long,
        val rating: Int,
        val content: String
)
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
        val oid: Long,
        val pid: Long,
        val rating: Double,
        val comment: String
)
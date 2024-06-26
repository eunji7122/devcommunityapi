package com.study.devcommunityapi.domain.comment.repository

import com.study.devcommunityapi.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @Query("SELECT c " +
            "FROM Comment c " +
            "JOIN CommentHierarchy p ON c.id = p.ancestorCommentId " +
            "WHERE c.post.id = :postId AND p.ancestorCommentId = p.descendantCommentId and c.deletedAt is null " +
            "Order by c.createdAt")
    fun findAllByPostId(postId: Long) : List<Comment>

    @Query("SELECT c " +
            "FROM Comment c " +
            "JOIN CommentHierarchy p ON c.id = p.descendantCommentId " +
            "WHERE p.ancestorCommentId = :commentId and c.deletedAt is null " +
            "Order by c.createdAt")
    fun findByCommentIdWithDescendant(@Param("commentId") commentId: Long): List<Comment>

    // 자식 댓글 모두 조회
    @Query("SELECT c, p FROM Comment c JOIN CommentHierarchy p ON c.id = p.descendantCommentId WHERE p.ancestorCommentId = :ancestorCommentId AND p.ancestorCommentId != p.descendantCommentId")
    fun findDescendantComments(@Param("ancestorCommentId") ancestorCommentId: Long) : List<Any>

    // 특정 게시글의 모든 댓글 조회 (좋아요 포함)
    @Query("SELECT c, NVL2(ch.comment.id, true, false) as heart " +
            "FROM Comment c " +
            "LEFT JOIN CommentHeart ch ON c.id = ch.comment.id AND ch.member.id = :memberId " +
            "WHERE c.post.id = :postId and c.deletedAt is null " +
            "ORDER BY c.createdAt")
    fun findCommentsByPostIdWithHeart(@Param("postId") postId: Long, @Param("memberId") memberId: Long): List<List<Any>>
}
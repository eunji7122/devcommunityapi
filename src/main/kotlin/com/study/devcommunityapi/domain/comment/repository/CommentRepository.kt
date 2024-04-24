package com.study.devcommunityapi.domain.comment.repository

import com.study.devcommunityapi.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Long> {

    @Query(value = "SELECT c FROM Comment c JOIN CommentPath p ON c.id = p.mainComment.id WHERE c.post.id = :postId AND p.mainComment.id = p.subComment.id Order by p.createdAt")
    fun findAllByPostId(postId: Long) : List<Comment>

    @Query(value = "SELECT c FROM Comment c JOIN CommentPath p ON c.id = p.subComment.id WHERE p.mainComment.id = :mainCommentId AND p.mainComment.id != p.subComment.id ORDER BY p.createdAt")
    fun findSubComments(@Param("mainCommentId") mainCommentId: Long) : List<Comment>
}
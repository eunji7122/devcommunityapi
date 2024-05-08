package com.study.devcommunityapi.domain.comment.repository

import com.study.devcommunityapi.domain.comment.entity.CommentHeart
import org.springframework.data.jpa.repository.JpaRepository

interface CommentHeartRepository: JpaRepository<CommentHeart, Long> {

    fun countByCommentId(commentId: Long): Int

    fun findByCommentIdAndMemberId(commentId: Long, memberId: Long): CommentHeart?

    fun deleteByCommentIdAndMemberId(commentId: Long, memberId: Long)
}
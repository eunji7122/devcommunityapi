package com.study.devcommunityapi.domain.comment.repository

import com.study.devcommunityapi.domain.comment.entity.CommentPath
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentPathRepository: JpaRepository<CommentPath, Long> {

    @Query(value = "SELECT p.subComment FROM CommentPath p where p.mainComment = :mainCommentId")
    fun findSubCommentIdAllByMainCommentId(@Param("mainCommentId") mainCommentId: Long) : List<Long>

    fun findByMainCommentIdAndSubCommentId(mainCommentId: Long, subCommentId: Long) : CommentPath

    fun findBySubCommentId(subCommentId: Long) : CommentPath?
}
package com.study.devcommunityapi.domain.comment.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table
class CommentPath(

    @ManyToOne
    @JoinColumn(name = "main_comment_id", nullable = false)
    val mainComment: Comment,

    @ManyToOne
    @JoinColumn(name = "sub_comment_id", nullable = false)
    val subComment: Comment,

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

//    fun toResponseDto(): CommentPathResponseDto = CommentPathResponseDto(id!!, mainComment.toResponseDto(), subComment.toResponseDto())
}
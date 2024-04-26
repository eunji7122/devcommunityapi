package com.study.devcommunityapi.domain.comment.entity

import jakarta.persistence.*

@Entity
@Table
class CommentHierarchy(

    @Column(nullable = false)
    val ancestorCommentId: Long,

    @Column(nullable = false)
    val descendantCommentId: Long,

    @Column(nullable = false)
    var depth: Int,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

//    fun toResponseDto(): CommentPathResponseDto = CommentPathResponseDto(id!!, mainComment.toResponseDto(), subComment.toResponseDto())
}
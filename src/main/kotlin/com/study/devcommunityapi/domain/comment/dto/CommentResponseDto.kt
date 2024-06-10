package com.study.devcommunityapi.domain.comment.dto

import com.study.devcommunityapi.domain.member.dto.MemberSummaryResponseDto
import java.time.LocalDateTime

data class CommentResponseDto(
    val id: Long,
    val contents: String,
    val ancestorCommentId: Long?,
    val descendantCommentId: Long?,
    val depth : Int?,
    val member: MemberSummaryResponseDto,
    val heartCount: Int,
    val updatedAt: LocalDateTime?,
)

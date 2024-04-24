package com.study.devcommunityapi.domain.comment.dto

import com.study.devcommunityapi.domain.member.dto.MemberSummaryResponseDto
import java.time.LocalDateTime

data class CommentResponseDto(
    val id: Long,
    val contents: String,
    val member: MemberSummaryResponseDto,
    val subComments: List<CommentResponseDto>?,
    val deletedAt: LocalDateTime?
)

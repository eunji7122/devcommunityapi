package com.study.devcommunityapi.domain.post.dto

import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import com.study.devcommunityapi.domain.member.dto.MemberSummaryResponseDto
import java.time.LocalDateTime

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val board: BoardResponseDto,
    val member: MemberSummaryResponseDto,
    val viewCount: Int,
    val heartCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

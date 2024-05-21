package com.study.devcommunityapi.domain.post.dto

import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import com.study.devcommunityapi.domain.member.dto.MemberSummaryResponseDto
import com.study.devcommunityapi.domain.post.entity.PostImage
import java.time.LocalDateTime

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val board: BoardResponseDto,
    val member: MemberSummaryResponseDto,
    val viewCount: Int,
    val heartCount: Int,
    val tags: List<String>?,
    val images: List<PostImage>,
    val isSelected: Boolean,
    val rewardPoint: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

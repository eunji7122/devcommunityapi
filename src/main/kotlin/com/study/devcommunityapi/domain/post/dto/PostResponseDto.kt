package com.study.devcommunityapi.domain.post.dto

import com.study.devcommunityapi.domain.board.dto.BoardResponseDto

data class PostResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val board: BoardResponseDto,
    val viewCount: Int,
)

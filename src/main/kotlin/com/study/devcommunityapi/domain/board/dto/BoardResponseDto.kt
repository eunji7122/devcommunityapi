package com.study.devcommunityapi.domain.board.dto

data class BoardResponseDto(

    val id: Long,
    val name: String,
    val path: String,
    val usingStatus: Boolean

)

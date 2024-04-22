package com.study.devcommunityapi.common.util.dto

data class PageResponseDto<E>(
    val dtoList: List<E>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

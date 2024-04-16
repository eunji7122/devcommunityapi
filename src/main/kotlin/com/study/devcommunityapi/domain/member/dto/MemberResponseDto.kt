package com.study.devcommunityapi.domain.member.dto

data class MemberResponseDto(
    val id: Long,
    val email: String,
    val name: String,
    val birthDate: String,
    val gender: String,
)

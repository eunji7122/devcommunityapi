package com.study.devcommunityapi.domain.member.dto

import com.study.devcommunityapi.domain.member.entity.MemberRole

data class MemberResponseDto(
    val id: Long,
    val email: String,
    val name: String,
    val birthDate: String,
    val gender: String,
    val point: Int,
    val roleNames: List<MemberRole>
)

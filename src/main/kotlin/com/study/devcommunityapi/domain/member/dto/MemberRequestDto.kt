package com.study.devcommunityapi.domain.member.dto

import com.study.devcommunityapi.domain.member.entity.Gender
import java.time.LocalDate

data class MemberRequestDto(
    val id: Long?,
    val email: String,
    val password: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender,
)

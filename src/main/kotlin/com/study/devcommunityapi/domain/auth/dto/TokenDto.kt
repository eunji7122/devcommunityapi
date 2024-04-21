package com.study.devcommunityapi.domain.auth.dto

data class TokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)

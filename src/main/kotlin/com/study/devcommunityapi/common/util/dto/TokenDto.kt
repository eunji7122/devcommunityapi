package com.study.devcommunityapi.common.util.dto

data class TokenDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
)

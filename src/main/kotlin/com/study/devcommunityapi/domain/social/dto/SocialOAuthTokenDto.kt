package com.study.devcommunityapi.domain.social.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SocialOAuthTokenDto(

    @JsonProperty("access_token")
    val access_token: String,
//    val refreshToken: String,

    @JsonProperty("expires_in")
    val expires_in: Int,

    @JsonProperty("scope")
    val scope: String,

    @JsonProperty("token_type")
    val token_type: String,

    @JsonProperty("id_token")
    val id_token: String
)

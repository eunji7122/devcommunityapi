package com.study.devcommunityapi.domain.social.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SocialUserInfoDto(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("verified_email")
    val verified_email: Boolean,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("given_name")
    val given_name: String,

    @JsonProperty("family_name")
    val family_name: String,

    @JsonProperty("picture")
    val picture: String,

    @JsonProperty("locale")
    val locale: String,
)

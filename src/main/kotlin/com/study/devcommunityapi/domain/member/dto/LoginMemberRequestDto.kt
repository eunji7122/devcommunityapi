package com.study.devcommunityapi.domain.member.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class LoginMemberRequestDto(

    @field:NotBlank
    @JsonProperty("email")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,

) {

    val loginId: String
        get() = _loginId!!
    val password: String
        get() = _password!!

}

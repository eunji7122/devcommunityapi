package com.study.devcommunityapi.domain.auth.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.auth.dto.TokenDto
import com.study.devcommunityapi.domain.auth.service.AuthService
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/token")
    fun signIn(@RequestBody @Valid loginMemberRequestDto: LoginMemberRequestDto): BaseResponseDto<TokenDto> {
        val token = authService.signIn(loginMemberRequestDto)
        return BaseResponseDto(data = token)
    }

    @PostMapping("/reissue")
    fun reissue(@RequestHeader("Authorization") authHeader:String, refreshToken: String): BaseResponseDto<TokenDto> {
        val token = authService.reissueToken(refreshToken)
        return BaseResponseDto(data = token)
    }
}
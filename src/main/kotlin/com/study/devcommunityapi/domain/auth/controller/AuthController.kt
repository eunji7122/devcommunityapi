package com.study.devcommunityapi.domain.auth.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.auth.dto.RefreshTokenDto
import com.study.devcommunityapi.domain.auth.dto.TokenDto
import com.study.devcommunityapi.domain.auth.service.AuthService
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    fun reissue(@RequestBody refreshTokenDto: RefreshTokenDto): BaseResponseDto<TokenDto> {
        val token = authService.reissueToken(refreshTokenDto.refreshToken)
        return BaseResponseDto(data = token)
    }
}
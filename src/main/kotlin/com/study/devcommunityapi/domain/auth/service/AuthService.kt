package com.study.devcommunityapi.domain.auth.service

import com.study.devcommunityapi.common.exception.BadCredentialsException
import com.study.devcommunityapi.common.exception.NotFoundTokenException
import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import com.study.devcommunityapi.domain.auth.dto.TokenDto
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import com.study.devcommunityapi.domain.member.service.MemberService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val memberService: MemberService,
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
) {

    fun signIn(loginMemberRequestDto: LoginMemberRequestDto): TokenDto {
        val foundMember = memberService.findMemberByEmail(loginMemberRequestDto.loginId)

        if (!memberService.checkPassword(loginMemberRequestDto.password, foundMember.password)) {
            throw BadCredentialsException()
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(loginMemberRequestDto.loginId, foundMember.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)

        val accessToken = jwtTokenProvider.createAccessToken(loginMemberRequestDto.loginId, authorities)

        // 리프레시 토큰이 있으면 삭제
        if (refreshTokenService.findByUsername(loginMemberRequestDto.loginId) != null) {
            refreshTokenService.deleteByUsername(loginMemberRequestDto.loginId)
        }

        val refreshToken = jwtTokenProvider.createRefreshToken(loginMemberRequestDto.loginId, authorities)
        refreshTokenService.saveRefreshToken(loginMemberRequestDto.loginId, refreshToken)

        return TokenDto(JwtTokenProvider.TOKEN_PREFIX, accessToken, refreshToken)
    }

    fun createToken(refreshToken: String): TokenDto {
        val claims = jwtTokenProvider.getClaims(refreshToken)

        val newAccessToken = jwtTokenProvider.createAccessToken(claims.subject, claims["auth"].toString())

        // accessToken, refreshToken 모두 재발행하는 경우
        if (jwtTokenProvider.getRemainTime(refreshToken) < JwtTokenProvider.REISSUE_EXPIRATION_MILLISECONDS) {
            refreshTokenService.deleteByRefreshToken(refreshToken)
            val newRefreshToken = jwtTokenProvider.createRefreshToken(claims.subject, claims["auth"].toString())
            refreshTokenService.saveRefreshToken(claims.subject, newRefreshToken)
            return TokenDto(JwtTokenProvider.TOKEN_PREFIX, newAccessToken, newRefreshToken)
        }

        // accessToken만 재발행하는 경우
        return TokenDto(JwtTokenProvider.TOKEN_PREFIX, newAccessToken, "")
    }

    fun reissueToken(refreshToken: String): TokenDto {
        refreshTokenService.findByRefreshToken(refreshToken) ?: throw NotFoundTokenException()
        return createToken(refreshToken)
    }
}
package com.study.devcommunityapi.domain.social.service

import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.service.MemberService
import com.study.devcommunityapi.domain.social.dto.SocialOAuthTokenDto
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service

@Service
class SocialService (
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
) {

    fun getRedirectUrl(socialType: String): String {
        val socialOAuth = findSocialType(socialType)
        return socialOAuth.getOAuthRedirectURL()
    }

    fun findSocialType(socialType: String): SocialOAuth {
        if (socialType == "google") {
            return GoogleOAuthImpl()
        } else {
            throw RuntimeException("알 수 없는 소셜 타입입니다.")
        }
    }

    fun requestAccessToken(socialType: String, code: String): ResponseEntity<String> {
        val socialOAuth = findSocialType(socialType)
        return socialOAuth.requestAccessToken(code)
    }

    fun oAuthLogin(socialType: String, code: String): SocialOAuthTokenDto {
        val socialOAuth = findSocialType(socialType)
        val accessTokenResponse = socialOAuth.requestAccessToken(code)
        val oAuthToken = socialOAuth.getAccessToken(accessTokenResponse)

        val userInfoResponse = socialOAuth.requestUserInfo(oAuthToken)
        val socialUser = socialOAuth.getUserInfo(userInfoResponse)

        val email = socialUser.email
        val name = socialUser.name

         memberService.createMember(
            MemberRequestDto(
                null,
                _email = email,
                _password = email,
                _name = name,
                "2000-01-01",
                "WOMAN"
            )
        )

        val member = memberService.findMemberByEmail(email)

        val authenticationToken = UsernamePasswordAuthenticationToken(email, member.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        val authorities: String = authentication
            .authorities
            .joinToString(",", transform = GrantedAuthority::getAuthority)

        val accessToken = jwtTokenProvider.createAccessToken(email, authorities)

        return SocialOAuthTokenDto(accessToken, oAuthToken.expires_in, oAuthToken.access_token, oAuthToken.token_type, oAuthToken.id_token)

    }

}
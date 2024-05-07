package com.study.devcommunityapi.domain.social.service

import com.study.devcommunityapi.domain.social.dto.SocialOAuthTokenDto
import com.study.devcommunityapi.domain.social.dto.SocialUserInfoDto
import org.springframework.http.ResponseEntity

interface SocialOAuth {

    fun getOAuthRedirectURL(): String

    fun requestAccessToken(code: String): ResponseEntity<String>

    fun getAccessToken(response: ResponseEntity<String>): SocialOAuthTokenDto

    fun requestUserInfo(oauthToken: SocialOAuthTokenDto): ResponseEntity<String>

    fun getUserInfo(userInfoResponse: ResponseEntity<String>): SocialUserInfoDto
}
package com.study.devcommunityapi.domain.social.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.study.devcommunityapi.domain.social.dto.SocialOAuthTokenDto
import com.study.devcommunityapi.domain.social.dto.SocialUserInfoDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class GoogleOAuthImpl : SocialOAuth {

    @Value("\${spring.security.oauth2.client.registration.google.client-id}")
    var googleClientId: String = ""

    @Value("\${spring.security.oauth2.client.registration.google.client-secret}")
    var googleClientSecret: String = ""

    @Value("\${spring.security.oauth2.client.registration.google.redirect-uri}")
    var googleRedirectUrl: String = ""


    override fun getOAuthRedirectURL(): String {
        return "http://localhost:8070/oauth2/authorization/google"
    }

    override fun requestAccessToken(code: String): ResponseEntity<String> {
        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams.set("code", code)
        queryParams.set("client_id", googleClientId)
        queryParams.set("client_secret", googleClientSecret)
        queryParams.set("redirect_uri", googleRedirectUrl)
        queryParams.set("grant_type", "authorization_code")

        val restTemplate = RestTemplate()

        val responseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token", queryParams, String::class.java)
        if (responseEntity.statusCode == HttpStatus.OK) {
            return responseEntity
        } else {
            throw RuntimeException("구글 로그인에 실패하였습니다.")
        }
    }

    override fun getAccessToken(response: ResponseEntity<String>): SocialOAuthTokenDto {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response.body, SocialOAuthTokenDto::class.java)
    }

    override fun requestUserInfo(oAuthToken: SocialOAuthTokenDto): ResponseEntity<String> {
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer " + oAuthToken.access_token)

        val uri = UriComponentsBuilder
            .fromUriString("https://www.googleapis.com/oauth2/v1/userinfo")
            .build().toUri()

        val request = HttpEntity<MultiValueMap<String, String>>(headers)

        val restTemplate = RestTemplate()
        return restTemplate.exchange(uri, HttpMethod.GET, request, String::class.java)
    }

    override fun getUserInfo(userInfoResponse: ResponseEntity<String>): SocialUserInfoDto {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(userInfoResponse.body, SocialUserInfoDto::class.java)
    }

}
package com.study.devcommunityapi.domain.social.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.social.dto.SocialOAuthTokenDto
import com.study.devcommunityapi.domain.social.service.SocialService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/social")
class SocialController(
    private val socialService: SocialService
) {

    @GetMapping("/google")
    fun moveGoogleInitUrl(response: HttpServletResponse) {
        response.sendRedirect("http://localhost:8070/oauth2/authorization/google")
    }

    @GetMapping("/google/redirect")
    fun redirectGoogleLogin(@RequestParam(value = "code") authCode: String): BaseResponseDto<SocialOAuthTokenDto> {
        return BaseResponseDto(socialService.oAuthLogin("google", authCode))
    }


}
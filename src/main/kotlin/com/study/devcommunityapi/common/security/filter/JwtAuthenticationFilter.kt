package com.study.devcommunityapi.common.security.filter

import com.nimbusds.jose.shaded.gson.Gson
import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = jwtTokenProvider.resolveToken(request)

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (_: Exception) {
            val gson = Gson()

            val msg = gson.toJson(mapOf("error" to "ERROR_ACCESS_TOKEN"))
            response.contentType = "application/json"

            val printWriter = response.writer
            printWriter.println(msg)
            printWriter.close()
        }

        filterChain.doFilter(request, response)
    }

    @Throws(ServletException::class)
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {

        // 필터를 거치지 않아야 하는 경로의 경우 ex: 로그인
        val path = request.requestURI
        return path.startsWith("/api/auth/")

        // return == false -> 체크함
        // return == true -> 체크 안함
    }
}
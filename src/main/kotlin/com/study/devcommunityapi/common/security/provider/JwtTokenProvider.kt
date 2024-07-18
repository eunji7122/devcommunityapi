package com.study.devcommunityapi.common.security.provider

import com.study.devcommunityapi.common.exception.IllegalArgumentException
import com.study.devcommunityapi.common.exception.JwtExpiredException
import com.study.devcommunityapi.common.exception.JwtMalformedException
import com.study.devcommunityapi.common.util.dto.CustomUser
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }

    companion object {
        // 1000 = 1초
        const val ACCESS_EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 1   // 10분
        const val REFRESH_EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30  // 30분
        const val REISSUE_EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 20  // 20분

        const val TOKEN_HEADER: String = "Authorization"
        const val TOKEN_PREFIX: String = "Bearer"
    }

    fun createToken(username: String, authority: String, expiration: Long): String {

        val now = Date()
        val tokenExpiration = Date(now.time + expiration)

        return Jwts.builder()
            .claim("auth", authority)
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(tokenExpiration)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

    }

    fun createAccessToken(username: String, authority: String): String {
        return createToken(username, authority, ACCESS_EXPIRATION_MILLISECONDS)
    }

    fun createRefreshToken(username: String, authority: String): String {
        return createToken(username, authority, REFRESH_EXPIRATION_MILLISECONDS)
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(TOKEN_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(7)
        } else {
            null
        }
    }

    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {} // Invalid JWT Token
                is JwtMalformedException -> {} // Invalid JWT Token
                is JwtExpiredException -> {} // Expired JWT Token
                is UnsupportedJwtException -> {} // Unsupported JWT Token
                is IllegalArgumentException -> {} // JWT claims string is empty
                else -> {} // else
            }
            println(e.message)
            throw e
        }
    }

    fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getClaims(token)
        val auth = claims["auth"] ?: throw JwtMalformedException()

        // 권한 정보 추출
        val authorities: Collection<GrantedAuthority> = (auth as String)
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        val principal: UserDetails =
            CustomUser(claims.subject, "", authorities)
//        val principal: UserDetails = customUserDetailsService.loadUserByUsername(token)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun getRemainTime(token: String): Long {
        val expiration: Date = getClaims(token).expiration
        val now = Date()
        return expiration.time - now.time
    }

}
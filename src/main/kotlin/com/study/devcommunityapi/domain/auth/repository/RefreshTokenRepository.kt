package com.study.devcommunityapi.domain.auth.repository

import com.study.devcommunityapi.domain.auth.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {

    fun findByRefreshToken(refreshToken: String): RefreshToken?

    fun deleteByRefreshToken(refreshToken: String)
}
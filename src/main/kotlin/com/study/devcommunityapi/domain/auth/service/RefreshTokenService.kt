package com.study.devcommunityapi.domain.auth.service

import com.study.devcommunityapi.domain.auth.entity.RefreshToken
import com.study.devcommunityapi.domain.auth.repository.RefreshTokenRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun saveRefreshToken(username: String, refreshToken: String) {
        refreshTokenRepository.save(RefreshToken(username, refreshToken))
    }

    fun findByRefreshToken(refreshToken: String): RefreshToken? {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
    }

    fun findByUsername(username: String): RefreshToken? {
        return refreshTokenRepository.findByIdOrNull(username)
    }

    fun deleteByRefreshToken(refreshToken: String) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken)
    }

    fun deleteByUsername(username: String) {
        refreshTokenRepository.deleteById(username)
    }

}
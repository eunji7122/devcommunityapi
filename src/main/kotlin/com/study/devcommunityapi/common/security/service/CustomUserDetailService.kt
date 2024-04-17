package com.study.devcommunityapi.common.security.service

import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.member.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findMemberWithRoles(username)
            ?.let { memberToUserDetails(it) }
            ?: throw UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다.")
    }

    private fun memberToUserDetails(member: Member): UserDetails =
        User(
            member.email,
            passwordEncoder.encode(member.password),
            member.memberRoleList.map { SimpleGrantedAuthority("ROLE_${it}") }
        )

}
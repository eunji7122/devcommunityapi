package com.study.devcommunityapi.domain.member.service

import com.study.devcommunityapi.common.util.exception.InvalidInputException
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.dto.MemberResponseDto
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.member.entity.MemberRole
import com.study.devcommunityapi.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val passwordEncoder: PasswordEncoder,
) {

    fun signUp(memberRequestDto: MemberRequestDto): MemberResponseDto {

        var member: Member? = memberRepository.findMemberWithRoles(memberRequestDto.email)
        if (member != null) {
            throw InvalidInputException("email", "이미 등록된 아이디 입니다.")
        }

        member = memberRequestDto.toEntity(passwordEncoder)
        member.addMemberRole(MemberRole.USER)

        return memberRepository.save(member).toResponseDto()
    }

    fun signIn(loginMemberRequestDto: LoginMemberRequestDto): Authentication {

        val foundMember = memberRepository.findMemberWithRoles(loginMemberRequestDto.loginId)
            ?: throw UsernameNotFoundException("회원 아이디(${loginMemberRequestDto.loginId})가 존재하지 않는 유저입니다.")

        if (!passwordEncoder.matches(loginMemberRequestDto.password, foundMember.password)) {
            throw UsernameNotFoundException("아이디 혹은 비밀번호를 확인하세요.")
        }

        // 사용자 인증을 위한 Authentication 토큰 객체 (인증 전)
        val authenticationToken = UsernamePasswordAuthenticationToken(loginMemberRequestDto.loginId, foundMember.password)

        // authenticationManagerBuilder를 통해 인증된 Authentication 객체를 반환
        // 인증 실패 시, 에러 발생
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        // 인증된 Authentication 객체를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().authentication = authentication

        return authentication
    }

    fun getMemberWithRoles(email: String): MemberResponseDto {
        val foundMember = memberRepository.findMemberWithRoles(email)
            ?: throw InvalidInputException("email", "회원 아이디(${email})가 존재하지 않는 유저입니다.")
        return foundMember.toResponseDto()
    }

    fun updateMember(memberRequestDto: MemberRequestDto): MemberResponseDto {
        val member = memberRequestDto.toEntity(passwordEncoder)
        return memberRepository.save(member).toResponseDto()
    }

}
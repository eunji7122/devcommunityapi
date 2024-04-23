package com.study.devcommunityapi.domain.member.service

import com.study.devcommunityapi.common.exception.InvalidInputException
import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.dto.MemberResponseDto
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.member.entity.MemberRole
import com.study.devcommunityapi.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createMember(memberRequestDto: MemberRequestDto): MemberResponseDto {

        var member: Member? = memberRepository.findMemberWithRoles(memberRequestDto.email)
        if (member != null) {
            throw InvalidInputException("email", "이미 등록된 아이디 입니다.")
        }

        member = memberRequestDto.toEntity(passwordEncoder)
        member.addMemberRole(MemberRole.USER)

        return memberRepository.save(member).toResponseDto()
    }

    fun findMemberWithRoles(email: String): MemberResponseDto {
        val foundMember = memberRepository.findMemberWithRoles(email)
            ?: throw InvalidInputException("email", "회원 아이디(${email})가 존재하지 않는 유저입니다.")
        return foundMember.toResponseDto()
    }

    fun findMemberByEmail(email: String): Member {
        val foundMember = memberRepository.findMemberWithRoles(email)
            ?: throw InvalidInputException("email", "회원 아이디(${email})가 존재하지 않는 유저입니다.")
        return foundMember
    }

    fun updateMember(memberRequestDto: MemberRequestDto): MemberResponseDto {
        val member = memberRequestDto.toEntity(passwordEncoder)
        return memberRepository.save(member).toResponseDto()
    }

    fun checkPassword(password: String, savedPassword: String): Boolean {
        return passwordEncoder.matches(password, savedPassword)
    }

}
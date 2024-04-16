package com.study.devcommunityapi.domain.member.service

import com.study.devcommunityapi.common.util.exception.InvalidInputException
import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.dto.MemberResponseDto
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.member.entity.MemberRole
import com.study.devcommunityapi.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    fun register(memberRequestDto: MemberRequestDto): MemberResponseDto {

        var member: Member? = memberRepository.getMemberWithRoles(memberRequestDto.email)
        if (member != null) {
            throw InvalidInputException("email", "이미 등록된 아이디 입니다.")
        }

        member = memberRequestDto.toEntity()
        member.addMemberRole(MemberRole.USER)

        return memberRepository.save(member).toResponseDto()
    }

    fun getMemberWithRoles(email: String): MemberResponseDto {
        val foundMember = memberRepository.getMemberWithRoles(email)
            ?: throw InvalidInputException("email", "회원 아이디(${email})가 존재하지 않는 유저입니다.")
        return foundMember.toResponseDto()
    }

    fun updateMember(memberRequestDto: MemberRequestDto): MemberResponseDto {
        val member = memberRequestDto.toEntity()
        return memberRepository.save(member).toResponseDto()
    }

}
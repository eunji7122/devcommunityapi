package com.study.devcommunityapi.domain.member.service

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

    fun register(memberRequestDto: MemberRequestDto): MemberResponseDto? {

        var member: Member? = memberRepository.getMemberWithRoles(memberRequestDto.email)
        if (member != null) {
            return null
        }

        member = Member(
            null,
            memberRequestDto.email,
            memberRequestDto.password,
            memberRequestDto.name,
            memberRequestDto.birthDate,
            memberRequestDto.gender,
        )

        member.addMemberRole(MemberRole.USER)

        return memberRepository.save(member).toResponseDto()
    }

    fun getMemberWithRoles(email: String): MemberResponseDto {
        val foundMember = memberRepository.getMemberWithRoles(email) ?: throw RuntimeException()
        return foundMember.toResponseDto()
    }

}
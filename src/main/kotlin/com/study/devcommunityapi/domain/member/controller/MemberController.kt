package com.study.devcommunityapi.domain.member.controller

import com.study.devcommunityapi.common.dto.BaseResponseDto
import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.dto.MemberResponseDto
import com.study.devcommunityapi.domain.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/signUp")
    fun signUp(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponseDto<MemberResponseDto> {
        val createdMember = memberService.signUp(memberRequestDto)
        return BaseResponseDto(data = createdMember)
    }

    @GetMapping("/me")
    fun getMe(email: String): BaseResponseDto<MemberResponseDto> {
        val foundMember = memberService.getMemberWithRoles(email)
        return BaseResponseDto(data = foundMember)
    }

    @PutMapping("/me")
    fun updateMe(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponseDto<MemberResponseDto> {
        val foundMember = memberService.updateMember(memberRequestDto)
        return BaseResponseDto(data = foundMember)
    }
}
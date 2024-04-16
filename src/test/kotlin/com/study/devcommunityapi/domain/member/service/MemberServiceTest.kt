package com.study.devcommunityapi.domain.member.service

import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.entity.MemberRole
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberServiceTest @Autowired constructor(
    val memberService: MemberService
) {

    @Test
    @Transactional
    @DisplayName("회원가입")
    fun register() {

        val createdMember = memberService.register(MemberRequestDto(
            null,
            "user1@test.com",
            "user1",
            "user1",
            "2000-01-01",
            "WOMAN"
        ))

        Assertions.assertThat(createdMember.email).isEqualTo("user1@test.com")

    }

    @Test
    @Transactional
    @DisplayName("회원 정보 조회")
    fun getMemberWithRoles() {

        val createdMember = memberService.register(MemberRequestDto(
            null,
            "user1@test.com",
            "user1",
            "user1",
            "2000-01-01",
            "WOMAN"
        ))

        val member = memberService.getMemberWithRoles(createdMember.email)

        Assertions.assertThat(member.email).isEqualTo("user1@test.com")
        Assertions.assertThat(member.roleNames[0]).isEqualTo(MemberRole.USER)
    }

    @Test
    @Transactional
    @DisplayName("회원 정보 수정")
    fun updateMember() {

        val createdMember = memberService.register(MemberRequestDto(
            null,
            "user1@test.com",
            "user1",
            "user1",
            "2000-01-01",
            "WOMAN"
        ))

        Assertions.assertThat(createdMember.name).isEqualTo("user1")

        val updateMember = memberService.updateMember(MemberRequestDto(
            createdMember.id,
            "user1@test.com",
            "user1",
            "user2",
            "2000-01-01",
            "WOMAN"
        ))

        Assertions.assertThat(updateMember.name).isEqualTo("user2")
    }
}
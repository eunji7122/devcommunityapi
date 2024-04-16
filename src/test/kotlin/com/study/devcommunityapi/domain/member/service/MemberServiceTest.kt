package com.study.devcommunityapi.domain.member.service

import com.study.devcommunityapi.domain.member.dto.MemberRequestDto
import com.study.devcommunityapi.domain.member.entity.Gender
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class MemberServiceTest @Autowired constructor(
    val memberService: MemberService
) {

    @Test
    @Transactional
    fun register() {

        val createdMember = memberService.register(MemberRequestDto(
            null,
            "user1@test.com",
            "user1",
            "user1",
            LocalDate.now(),
            Gender.MAN
        ))

        Assertions.assertThat(createdMember!!.email).isEqualTo("user1@test.com")

    }

    @Test
    fun getMemberWithRoles() {
    }
}
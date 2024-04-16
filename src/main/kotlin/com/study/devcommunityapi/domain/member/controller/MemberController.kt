package com.study.devcommunityapi.domain.member.controller

import com.study.devcommunityapi.domain.member.service.MemberService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberService: MemberService
) {

}
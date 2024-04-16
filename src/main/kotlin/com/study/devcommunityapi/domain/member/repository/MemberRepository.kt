package com.study.devcommunityapi.domain.member.repository

import com.study.devcommunityapi.domain.member.entity.Member
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository: JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = ["memberRoleList"])
    @Query("select m from Member m where m.email = :email")
    fun getMemberWithRoles(@Param("email") email: String): Member?
}
package com.study.devcommunityapi.domain.member.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.member.dto.MemberResponseDto
import jakarta.persistence.*
import lombok.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ArrayList

@Entity
@Table
@ToString(exclude = ["memberRoleList"])
class Member(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(nullable = false, unique = true, length = 50, updatable = false)
    var email: String,

    @Column(nullable = false, length = 100)
    var password: String,

    @Column(nullable = false, length = 10)
    var name: String,

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    var birthDate: LocalDate,

    @Column(nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_role", joinColumns = [JoinColumn(name = "id")])
    var memberRoleList: MutableList<MemberRole> = ArrayList()

): BaseEntity() {

    private fun LocalDate.formatDate(): String =
        this.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

    fun addMemberRole(role: MemberRole) {
        memberRoleList.add(role)
    }

    fun clearRole() {
        memberRoleList.clear()
    }

    fun toResponseDto(): MemberResponseDto =
        MemberResponseDto(
            id!!,
            email,
            name,
            birthDate.formatDate(),
            gender.desc,
            memberRoleList
        )

}
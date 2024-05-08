package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.member.entity.Member
import jakarta.persistence.*


@Entity
@Table
class PostHeart (

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
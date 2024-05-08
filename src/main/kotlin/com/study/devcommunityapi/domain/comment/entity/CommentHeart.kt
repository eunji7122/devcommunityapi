package com.study.devcommunityapi.domain.comment.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.member.entity.Member
import jakarta.persistence.*

@Entity
@Table
class CommentHeart(

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    val comment: Comment,

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}
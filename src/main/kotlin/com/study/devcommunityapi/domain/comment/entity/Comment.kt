package com.study.devcommunityapi.domain.comment.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.comment.dto.CommentResponseDto
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.entity.Post
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import java.time.LocalDateTime

@Entity
@Table
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id = ?")
class Comment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var contents: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member,

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

): BaseEntity() {

    @Column(nullable = true)
    val deletedAt: LocalDateTime? = null

    fun toResponseDto(heartCount: Int = 0): CommentResponseDto
    = CommentResponseDto(id!!, contents, member.toSummaryResponseDto(), heartCount, deletedAt)

}
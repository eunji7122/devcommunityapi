package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.board.entity.Board
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import java.time.LocalDateTime

@Entity
@Table
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id = ?")
class Post (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var content: String,

    @ManyToOne @JoinColumn(name = "board_id", nullable = false)
    var board: Board,

    @ManyToOne @JoinColumn(name = "member_id", nullable = false)
    var member: Member,

    @Column(nullable = false)
    var viewCount: Int

): BaseEntity() {

    @Column(nullable = true)
    val deletedAt: LocalDateTime? = null

    fun toResponseDto(): PostResponseDto = PostResponseDto(id!!, title, content, board.toResponseDto(), member.toResponseDto(), viewCount, createdAt, updatedAt)

}
package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import com.study.devcommunityapi.domain.board.entity.Board
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
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
    var viewCount: Int,

    @Column
    @ColumnDefault("false")
    var isSelected: Boolean,

    @Column
    @ColumnDefault("0")
    var rewardPoint: Int,

    @ElementCollection
    @CollectionTable(name = "POST_IMAGE", joinColumns = [JoinColumn(name = "post_id")])
    var images: List<PostImage> = arrayListOf()

): BaseEntity() {

    @Column(nullable = true)
    val deletedAt: LocalDateTime? = null

    fun toResponseDto(heartCount: Int = 0, tags: List<String> = arrayListOf()): PostResponseDto
        = PostResponseDto(id!!, title, content, board.toResponseDto(), member.toSummaryResponseDto(), viewCount, heartCount, tags, images, isSelected, rewardPoint, createdAt, updatedAt)
}
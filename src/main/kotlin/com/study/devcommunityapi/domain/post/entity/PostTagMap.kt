package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table
class PostTagMap (

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    val tag: Tag

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
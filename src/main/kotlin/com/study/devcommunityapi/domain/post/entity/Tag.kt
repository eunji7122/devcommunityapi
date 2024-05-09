package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table
class Tag (

    @Column(nullable = false)
    var name: String

): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
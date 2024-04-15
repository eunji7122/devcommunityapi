package com.study.devcommunityapi.domain.board.entity

import com.study.devcommunityapi.common.entity.BaseEntity
import com.study.devcommunityapi.domain.board.dto.BoardResponseDto
import jakarta.persistence.*

@Entity
@Table
class Board (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var usingStatus: Boolean
): BaseEntity() {

    fun toResponseDto(): BoardResponseDto = BoardResponseDto(id!!, name, usingStatus)
}
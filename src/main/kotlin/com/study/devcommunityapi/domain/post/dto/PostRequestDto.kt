package com.study.devcommunityapi.domain.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.devcommunityapi.domain.board.entity.Board
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.entity.Post
import jakarta.validation.constraints.NotBlank

data class PostRequestDto(

    val id: Long?,

    @field:NotBlank
    @JsonProperty("title")
    private val _title: String?,

    @field:NotBlank
    @JsonProperty("content")
    private val _content: String?,

    @field:NotBlank
    @JsonProperty("boardId")
    private val _boardId: Long?,

    @field:NotBlank
    @JsonProperty("viewCount")
    private val _viewCount: Int?,

    @JsonProperty("tags")
    private val _tags: String?,

    @JsonProperty("isSelected")
    private val _isSelected: Boolean? = false,

    @JsonProperty("rewardPoint")
    private val _rewardPoint: Int? = 0,

) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!

    val boardId: Long
        get() = _boardId!!

    val viewCount: Int
        get() = _viewCount!!

    val tags: String
        get() = _tags!!

    val isSelected: Boolean
        get() = _isSelected!!

    val rewardPoint: Int
        get() = _rewardPoint!!


    fun toEntity(board: Board, member: Member): Post = Post(id, title, content, board, member, viewCount, isSelected, rewardPoint)

}

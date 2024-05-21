package com.study.devcommunityapi.domain.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.devcommunityapi.domain.board.entity.Board
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.entity.PostImage
import jakarta.validation.constraints.NotBlank

data class PostRequestDto(

    val id: Long?,

    @field:NotBlank
    @JsonProperty("title")
    val title: String,

    @field:NotBlank
    @JsonProperty("content")
    val content: String,

    @field:NotBlank
    @JsonProperty("boardId")
    val boardId: Long,

    @field:NotBlank
    @JsonProperty("viewCount")
    val viewCount: Int? = 0,

    @JsonProperty("tags")
    val tags: String? = "",

    @JsonProperty("isSelected")
    val isSelected: Boolean? = false,

    @JsonProperty("rewardPoint")
    val rewardPoint: Int? = 0,

) {
//    val title: String
//        get() = _title!!
//
//    val content: String
//        get() = _content!!
//
//    val boardId: Long
//        get() = _boardId!!
//
//    val viewCount: Int
//        get() = _viewCount!!
//
//    val tags: String
//        get() = _tags!!
//
//    val isSelected: Boolean
//        get() = _isSelected!!
//
//    val rewardPoint: Int
//        get() = _rewardPoint!!


    fun toEntity(board: Board, member: Member, uploadedFiles: List<PostImage> = arrayListOf()): Post
        = Post(id, title, content, board, member, viewCount!!, isSelected!!, rewardPoint!!, uploadedFiles)

}

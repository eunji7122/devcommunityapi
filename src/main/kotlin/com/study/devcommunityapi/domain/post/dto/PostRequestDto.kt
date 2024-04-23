package com.study.devcommunityapi.domain.post.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.devcommunityapi.common.util.dto.PageRequestDto
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
    @JsonProperty("memberId")
    private val _memberId: Long?,

    @field:NotBlank
    @JsonProperty("viewCount")
    private val _viewCount: Int?,

    @JsonProperty("pageRequestDto")
    private val _pageRequestDto: PageRequestDto?,

) {
    val title: String
        get() = _title!!

    val content: String
        get() = _content!!

    val boardId: Long
        get() = _boardId!!

    val memberId: Long
        get() = _memberId!!

    val viewCount: Int
        get() = _viewCount!!

//    val pageRequestDto: PageRequestDto
//        get() = _pageRequestDto!!
    val pageRequestDto: PageRequestDto
        get() {
            if (_pageRequestDto == null)
                return PageRequestDto(1, 10)
            return _pageRequestDto
        }

    fun toEntity(board: Board, member: Member): Post = Post(id, title, content, board, member, viewCount)

}

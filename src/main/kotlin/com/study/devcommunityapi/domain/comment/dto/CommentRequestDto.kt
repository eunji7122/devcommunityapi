package com.study.devcommunityapi.domain.comment.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.study.devcommunityapi.domain.comment.entity.Comment
import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.entity.Post
import jakarta.validation.constraints.NotBlank

data class CommentRequestDto(
    val id: Long?,

    @field:NotBlank
    @JsonProperty("contents")
    private val _contents: String?,

    @field:NotBlank
    @JsonProperty("memberId")
    private val _memberId: Long?,

    @field:NotBlank
    @JsonProperty("postId")
    private val _postId: Long?,

    val ancestorCommentId: Long?,

    @JsonProperty("isSelected")
    private val _isSelected: Boolean? = false,
) {
    val contents: String
        get() = _contents!!

    val memberId: Long
            get() = _memberId!!

    val postId: Long
        get() = _postId!!

    val isSelected: Boolean
        get() = _isSelected!!

    fun toEntity(member: Member, post: Post): Comment = Comment(id, contents, member, post, isSelected)
}

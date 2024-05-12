package com.study.devcommunityapi.domain.post.entity

import com.study.devcommunityapi.domain.post.dto.PostResponseDto

interface PostWithHeartCount {
    var post: Post
    var heartCount: Int
}

fun PostWithHeartCount.toResponseDto(tags: List<String> = arrayListOf()): PostResponseDto = PostResponseDto(
    post.id!!,
    post.title,
    post.content,
    post.board.toResponseDto(),
    post.member.toSummaryResponseDto(),
    post.viewCount,
    heartCount,
    tags,
    post.isSelected,
    post.rewardPoint,
    post.createdAt,
    post.updatedAt
)

fun PostWithHeartCount.toPostIdDto(): Long = post.id!!
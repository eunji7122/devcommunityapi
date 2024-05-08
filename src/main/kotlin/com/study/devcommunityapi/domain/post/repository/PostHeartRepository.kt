package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.PostHeart
import org.springframework.data.jpa.repository.JpaRepository

interface PostHeartRepository: JpaRepository<PostHeart, Long> {

    fun countByPostId(postId: Long): Int

    fun findByPostIdAndMemberId(postId: Long, memberId: Long): PostHeart?

    fun findAllByMemberId(memberId: Long): List<PostHeart>

    fun deleteByPostIdAndMemberId(postId: Long, memberId: Long)
}
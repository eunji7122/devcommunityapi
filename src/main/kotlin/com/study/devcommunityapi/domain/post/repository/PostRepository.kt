package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun findAllByBoardId(boardId: Long) : List<Post>
}
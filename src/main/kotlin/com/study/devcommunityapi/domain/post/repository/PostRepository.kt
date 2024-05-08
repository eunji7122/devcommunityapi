package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun findAllByBoardId(boardId: Long, pageable: Pageable) : Page<Post>

}
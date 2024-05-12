package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.entity.PostWithHeartCount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {

    @Query(value = "SELECT p post, COUNT(ph) heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id GROUP BY p.id order by p.updatedAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByNewest(boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p post, COUNT(ph) heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id GROUP BY p.id order by p.viewCount DESC, p.updatedAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByViews(boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p post, COUNT(ph) heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id GROUP BY p.id order by heartCount DESC, p.updatedAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByHeartCount(boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    fun findAllByBoardId(boardId: Long, pageable: Pageable) : Page<Post>

}
package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.entity.PostWithHeartCount
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostRepository : JpaRepository<Post, Long> {

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null GROUP BY p.id order by p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByNewest(@Param("boardId") boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null GROUP BY p.id order by p.viewCount DESC, p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByViews(@Param("boardId") boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null GROUP BY p.id order by heartCount DESC, p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByHeartCount(@Param("boardId") boardId: Long, pageable: Pageable): Page<PostWithHeartCount>

    fun findAllByBoardId(boardId: Long, pageable: Pageable) : Page<Post>

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null AND UPPER(p.title) LIKE concat('%', upper(:searchKeyword), '%') GROUP BY p.id order by p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByNewest(@Param("boardId") boardId: Long, @Param("searchKeyword") searchKeyword: String, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null AND UPPER(p.title) LIKE concat('%', upper(:searchKeyword), '%') GROUP BY p.id order by p.viewCount DESC, p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByViews(@Param("boardId") boardId: Long, @Param("searchKeyword") searchKeyword: String, pageable: Pageable): Page<PostWithHeartCount>

    @Query(value = "SELECT p as post, COUNT(ph) as heartCount FROM Post p LEFT JOIN PostHeart ph on p.id = ph.post.id WHERE p.board.id = :boardId and p.deletedAt is null AND UPPER(p.title) LIKE concat('%', upper(:searchKeyword), '%') GROUP BY p.id order by heartCount DESC, p.createdAt DESC")
    fun findAllByBoardIdWithHeartCountOrderByHeartCount(@Param("boardId") boardId: Long, @Param("searchKeyword") searchKeyword: String, pageable: Pageable): Page<PostWithHeartCount>

}
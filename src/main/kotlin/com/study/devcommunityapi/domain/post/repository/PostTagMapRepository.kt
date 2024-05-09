package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.PostTagMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostTagMapRepository: JpaRepository<PostTagMap, Long> {

    fun findByPostIdAndTagId(postId: Long, tagId: Long): PostTagMap?

    fun findByPostId(postId: Long): List<PostTagMap>?

    fun deleteByPostIdAndTagId(postId: Long, tagId: Long)

    @Query(value = "SELECT m.tag.name as name, count(m.post.id) as count FROM PostTagMap m group by m.tag.id")
    fun findTagsByGroupByTagId(): List<Map<String, Long>>
}
package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.PostTagMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PostTagMapRepository: JpaRepository<PostTagMap, Long> {

    fun findByPostIdAndTagId(postId: Long, tagId: Long): PostTagMap?

    fun findByPostId(postId: Long): List<PostTagMap>?

    fun deleteByPostIdAndTagId(postId: Long, tagId: Long)

    @Query(value = "SELECT m.tag.name as name, count(m.post.id) as count FROM PostTagMap m group by m.tag.id")
    fun findTagsByGroupByTagId(): List<Map<String, Long>>

    @Query(value = "SELECT ptm, t FROM PostTagMap ptm LEFT JOIN Tag t on t.id = ptm.tag.id WHERE ptm.post.id IN (:postIdList)")
    fun findByPostIdList(@Param("postIdList") postIdList: List<Long>): List<PostTagMap>
}
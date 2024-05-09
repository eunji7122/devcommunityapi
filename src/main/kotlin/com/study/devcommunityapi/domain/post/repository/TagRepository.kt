package com.study.devcommunityapi.domain.post.repository

import com.study.devcommunityapi.domain.post.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {

    fun findByName(name: String): Tag?

    fun deleteByName(name: String)

}
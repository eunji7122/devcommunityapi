package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.entity.PostTagMap
import com.study.devcommunityapi.domain.post.entity.Tag
import com.study.devcommunityapi.domain.post.repository.PostTagMapRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PostTagMapService (
    private val postTagMapRepository: PostTagMapRepository,
) {

    fun savePostTagMap(post: Post, tag: Tag): PostTagMap? {
        if (postTagMapRepository.findByPostIdAndTagId(post.id!!, tag.id!!) == null) {
            return postTagMapRepository.save(PostTagMap(post, tag))
        }
        return null
    }

    fun getTagsByPost(postId: Long): List<PostTagMap>? {
        return postTagMapRepository.findByPostId(postId)
    }

    fun deletePostTagMaps(postId: Long, postTagMaps: List<PostTagMap>) {
        // 게시글에 등록되어있는 tag 일괄 삭제
        for (map in postTagMaps) {
            postTagMapRepository.deleteByPostIdAndTagId(postId, map.tag.id!!)
        }
    }

    fun getPostTagMapsSortByMost(): List<Map<String, Long>> {
        return postTagMapRepository.findTagsByGroupByTagId()
    }

    fun getAllByPostIdList(postIdList: List<Long>): List<PostTagMap> {
        return postTagMapRepository.findByPostIdList(postIdList)
    }

}
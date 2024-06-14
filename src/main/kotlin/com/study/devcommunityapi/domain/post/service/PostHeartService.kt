package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.domain.member.entity.Member
import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.entity.PostHeart
import com.study.devcommunityapi.domain.post.repository.PostHeartRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PostHeartService(
    private val postHeartRepository: PostHeartRepository,
) {

    fun getPostHeartByMember(postId: Long, memberId: Long): Boolean {
        return postHeartRepository.findByPostIdAndMemberId(postId, memberId) != null
    }

    fun savePostHeart(post: Post, member: Member) {
        if (postHeartRepository.findByPostIdAndMemberId(post.id!!, member.id!!) != null) {
            throw RuntimeException("이미 좋아요를 등록한 게시글입니다.")
        }
        postHeartRepository.save(PostHeart(post, member))
    }

    fun deletePostHeart(postId: Long, memberId: Long) {
        if (postHeartRepository.findByPostIdAndMemberId(postId, memberId) == null) {
            throw RuntimeException("해당 게시글의 좋아요 등록 정보를 찾을 수 없습니다.")
        }
        postHeartRepository.deleteByPostIdAndMemberId(postId, memberId)
    }

    fun getHeartCountByPost(postId: Long): Int {
        return postHeartRepository.countByPostId(postId)
    }

    fun getHeartCountByMember(memberId: Long): List<PostHeart> {
        return postHeartRepository.findAllByMemberId(memberId)
    }

}
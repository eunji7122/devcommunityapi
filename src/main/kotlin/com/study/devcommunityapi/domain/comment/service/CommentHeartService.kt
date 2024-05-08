package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.domain.comment.entity.Comment
import com.study.devcommunityapi.domain.comment.entity.CommentHeart
import com.study.devcommunityapi.domain.comment.repository.CommentHeartRepository
import com.study.devcommunityapi.domain.member.entity.Member
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentHeartService(
    private val commentHeartRepository: CommentHeartRepository
) {

    fun saveCommentHeart(comment: Comment, member: Member) {
        if (commentHeartRepository.findByCommentIdAndMemberId(comment.id!!, member.id!!) != null) {
            throw RuntimeException("이미 좋아요를 등록한 댓글입니다.")
        }
        commentHeartRepository.save(CommentHeart(comment, member))
    }

    fun getHeartCountByComment(commentId: Long): Int {
        return commentHeartRepository.countByCommentId(commentId)
    }

    fun deleteCommentHeart(commentId: Long, memberId: Long) {
        if (commentHeartRepository.findByCommentIdAndMemberId(commentId, memberId) == null) {
            throw RuntimeException("이미 좋아요를 취소한 댓글입니다.")
        }
        commentHeartRepository.deleteByCommentIdAndMemberId(commentId, memberId)
    }
}
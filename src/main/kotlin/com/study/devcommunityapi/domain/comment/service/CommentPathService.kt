package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.domain.comment.entity.Comment
import com.study.devcommunityapi.domain.comment.entity.CommentPath
import com.study.devcommunityapi.domain.comment.repository.CommentPathRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentPathService(
    private val commentPathRepository: CommentPathRepository
) {

    fun saveCommentPath(mainComment: Comment, subComment: Comment) {
        commentPathRepository.save(CommentPath(mainComment, subComment))
    }
    fun deleteCommentPath(commentPathId: Long) {
        commentPathRepository.deleteById(commentPathId)
    }
}
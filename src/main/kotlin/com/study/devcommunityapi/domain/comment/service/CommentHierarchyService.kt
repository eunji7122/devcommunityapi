package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.domain.comment.entity.CommentHierarchy
import com.study.devcommunityapi.domain.comment.repository.CommentHierarchyRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentHierarchyService(
    private val commentHierarchyRepository: CommentHierarchyRepository
) {

    fun saveSelfCommentHierarchy(commentId: Long) {
        commentHierarchyRepository.save(CommentHierarchy(commentId, commentId, 0))
    }

    fun saveAncestorCommentsHierarchy(ancestorCommentId: Long, descendantCommentId: Long) {
        commentHierarchyRepository.insertCommentHierarchy(ancestorCommentId, descendantCommentId)
    }
}
package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.common.exception.NotFoundPostException
import com.study.devcommunityapi.domain.comment.entity.CommentHierarchy
import com.study.devcommunityapi.domain.comment.repository.CommentHierarchyRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
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

    fun getCommentHierarchy(commentId: Long): CommentHierarchy {
        return commentHierarchyRepository.findByIdOrNull(commentId) ?: throw NotFoundPostException()
    }

    fun getCommentHierarchies(commentId: Long): List<CommentHierarchy> {
        return commentHierarchyRepository.findAllByDescendantCommentId(commentId)
    }
}
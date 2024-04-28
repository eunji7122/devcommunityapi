package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.common.exception.NotFoundCommentException
import com.study.devcommunityapi.common.exception.NotFoundMemberException
import com.study.devcommunityapi.common.util.dto.CustomUser
import com.study.devcommunityapi.domain.comment.dto.CommentRequestDto
import com.study.devcommunityapi.domain.comment.dto.CommentResponseDto
import com.study.devcommunityapi.domain.comment.repository.CommentRepository
import com.study.devcommunityapi.domain.member.service.MemberService
import com.study.devcommunityapi.domain.post.service.PostService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val commentHierarchyService: CommentHierarchyService,
    private val memberService: MemberService,
    private val postService: PostService,
) {
    fun createComment(commentRequestDto: CommentRequestDto): CommentResponseDto {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        val foundPost = postService.getPostEntity(commentRequestDto.postId)

        val createdComment = commentRepository.save(commentRequestDto.toEntity(foundMember, foundPost))
        commentHierarchyService.saveSelfCommentHierarchy(createdComment.id!!)

        // 부모 댓글이 존재하는 경우
        if (commentRequestDto.ancestorCommentId != null) {
            // 부모 댓글 가져오기
            val foundAncestorComment = commentRepository.findByIdOrNull(commentRequestDto.ancestorCommentId)
                ?: throw NotFoundCommentException()

            if (foundAncestorComment.id != null) {
                commentHierarchyService.saveAncestorCommentsHierarchy(foundAncestorComment.id, createdComment.id)
            }
        }

        return createdComment.toResponseDto()
    }

    fun getCommentsByPostId(postId: Long): List<CommentResponseDto> {
//        val foundComments = commentRepository.findAllByPostId(postId)
//
//        return foundComments.stream().map {
//            val foundDescendantComments = commentRepository.findDescendantComments(it.id!!)
//            it.toResponseDto(foundDescendantComments.stream().map { subIt ->
//                subIt.toResponseDto()
//            }.toList())
//        }.toList()

        return commentRepository.findAllByPostId(postId).stream().map { it.toResponseDto() }.toList()
    }

    fun getComment(commentId: Long): CommentResponseDto? {
//        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw RuntimeException("존재하지 않는 댓글입니다.")
//        val descendantComments = commentRepository.findDescendantComments(commentId)
//        return foundComment.toResponseDto(descendantComments.stream().map {
//            it.toResponseDto()
//        }.toList())

        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException()
        return foundComment.toResponseDto()
    }

    fun getCommentWithDescendant(commentId: Long): List<CommentResponseDto>? {
        return commentRepository.findByCommentIdWithDescendant(commentId).stream().map { it.toResponseDto() }.toList()
    }

    fun updateComment(commentRequestDto: CommentRequestDto): CommentResponseDto {
        val foundComment = commentRepository.findByIdOrNull(commentRequestDto.id) ?: throw NotFoundCommentException()

        foundComment.contents = commentRequestDto.contents
        return foundComment.toResponseDto()
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }
}
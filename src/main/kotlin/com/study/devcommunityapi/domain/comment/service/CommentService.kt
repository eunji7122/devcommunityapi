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
    private val commentHeartService: CommentHeartService,
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

        return commentRepository.findAllByPostId(postId).stream().map {
            val heartCount = commentHeartService.getHeartCountByComment(it.id!!)
            it.toResponseDto(heartCount)
        }.toList()
    }

    fun getComment(commentId: Long): CommentResponseDto? {
//        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw RuntimeException("존재하지 않는 댓글입니다.")
//        val descendantComments = commentRepository.findDescendantComments(commentId)
//        return foundComment.toResponseDto(descendantComments.stream().map {
//            it.toResponseDto()
//        }.toList())

        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException()
        val heartCount = commentHeartService.getHeartCountByComment(foundComment.id!!)
        return foundComment.toResponseDto(heartCount)
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

    fun saveCommentHeart(commentId: Long) {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException()

        commentHeartService.saveCommentHeart(foundComment, foundMember)
    }

    fun deleteCommentHeart(commentId: Long) {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundMemberException()
        val foundMember = memberService.findMemberByEmail(username)

        commentHeartService.deleteCommentHeart(commentId, foundMember.id!!)
    }

    fun selectComment(commentRequestDto: CommentRequestDto) {
        // 댓글 채택
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundMemberException()
        val loginMember = memberService.findMemberByEmail(username)

        val foundComment = commentRepository.findByIdOrNull(commentRequestDto.id) ?: throw NotFoundCommentException()
        val foundPost = postService.getPostEntity(commentRequestDto.postId)

        if (foundPost.isSelected) {
            throw RuntimeException("이미 채택된 게시글입니다.")
        }

        if (loginMember.email != foundPost.member.email) {
            throw RuntimeException("로그인 회원과 게시글의 글쓴이가 일치하지 않습니다.")
        }

        if (foundPost.member.point < foundPost.rewardPoint) {
            throw RuntimeException("지급할 포인트가 보유한 포인트보다 많습니다.")
        }

        foundPost.member.point -= foundPost.rewardPoint
        foundComment.member.point += foundPost.rewardPoint
        foundPost.isSelected = true
        foundComment.isSelected = true

        postService.savePost(foundPost)
        commentRepository.save(foundComment)
        memberService.saveMember(foundPost.member)
        memberService.saveMember(foundComment.member)
    }
}
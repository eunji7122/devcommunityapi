package com.study.devcommunityapi.domain.comment.service

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
    private val commentPathService: CommentPathService,
    private val memberService: MemberService,
    private val postService: PostService,
) {

    fun createComment(commentRequestDto: CommentRequestDto): CommentResponseDto {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw RuntimeException("로그인 정보가 없습니다.")
        val foundMember = memberService.findMemberByEmail(username)
        val foundPost = postService.getPostEntity(commentRequestDto.postId)

        val createdComment = commentRepository.save(commentRequestDto.toEntity(foundMember, foundPost))

        // 부모 댓글이 존재하는 경우
        if (commentRequestDto.mainCommentId != null) {
            // 부모 댓글 가져오기
            val foundMainComment = commentRepository.findByIdOrNull(commentRequestDto.mainCommentId)
                ?: throw RuntimeException("존재하지 않는 댓글입니다.")
            commentPathService.saveCommentPath(foundMainComment, createdComment)
        }

        commentPathService.saveCommentPath(createdComment, createdComment)

        return createdComment.toResponseDto()
    }

    fun getCommentsByPostId(postId: Long): List<CommentResponseDto> {
        val foundComments = commentRepository.findAllByPostId(postId)

        return foundComments.stream().map {
            val foundSubComments = commentRepository.findSubComments(it.id!!)
            it.toResponseDto(foundSubComments.stream().map { subIt ->
                subIt.toResponseDto()
            }.toList())
        }.toList()
    }

    fun getComment(commentId: Long): CommentResponseDto? {
        val foundComment = commentRepository.findByIdOrNull(commentId) ?: throw RuntimeException("존재하지 않는 댓글입니다.")
        val subComments = commentRepository.findSubComments(commentId)
        return foundComment.toResponseDto(subComments.stream().map {
            it.toResponseDto()
        }.toList())
    }

    fun updateComment(commentRequestDto: CommentRequestDto): CommentResponseDto {
        val foundComment = commentRepository.findByIdOrNull(commentRequestDto.id) ?: throw RuntimeException("존재하지 않는 댓글입니다.")

        foundComment.contents = commentRequestDto.contents
        return foundComment.toResponseDto()
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }
}
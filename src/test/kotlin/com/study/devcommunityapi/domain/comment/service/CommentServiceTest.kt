package com.study.devcommunityapi.domain.comment.service

import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import com.study.devcommunityapi.domain.auth.service.AuthService
import com.study.devcommunityapi.domain.comment.dto.CommentRequestDto
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class CommentServiceTest @Autowired constructor(
    val commentService: CommentService,
    val authService: AuthService,
    val jwtTokenProvider: JwtTokenProvider
) {

    @BeforeEach
    fun setLoginMember() {
        val token = authService.signIn(LoginMemberRequestDto("user1@test.com", "password"))
        val authentication = jwtTokenProvider.getAuthentication(token.accessToken)
        SecurityContextHolder.getContext().authentication = authentication
    }

    @Test
    @DisplayName("댓글 생성")
    fun createComment() {
        for (i: Int in 1..5)
        {
            val commentDto = CommentRequestDto(
                null,
                "comment content $i",
                1,
                1,
                null
            )

            commentService.createComment(commentDto)
        }

    }

    @Test
    @DisplayName("댓글 조회")
    fun getComment() {
        val comment = commentService.getComment(15)
        Assertions.assertThat(comment!!.contents).isEqualTo("main comment content")
    }

    @Test
    @DisplayName("모든 댓글 조회")
    fun getCommentsByPostId() {
        val comments = commentService.getCommentsByPostId(2)
        Assertions.assertThat(comments.size).isEqualTo(1)
    }

    @Test
    @DisplayName("댓글 수정")
    fun updateComment() {
        val commentDto = CommentRequestDto(
            17,
            "main comment content",
            1,
            1,
            null
        )

        val updatedComment = commentService.updateComment(commentDto)
        Assertions.assertThat(updatedComment.contents).isEqualTo("main comment content")
    }

    @Test
    @DisplayName("댓글 삭제")
    fun deleteComment() {
        commentService.deleteComment(19)

        val deletedComment = commentService.getComment(19)
        Assertions.assertThat(deletedComment!!.deletedAt).isNotNull()
    }

}
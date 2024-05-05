package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.common.security.provider.JwtTokenProvider
import com.study.devcommunityapi.common.util.dto.PageRequestDto
import com.study.devcommunityapi.domain.auth.service.AuthService
import com.study.devcommunityapi.domain.member.dto.LoginMemberRequestDto
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder

@SpringBootTest
class PostServiceTest @Autowired constructor(
    val postService: PostService,
    val authService: AuthService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @BeforeEach
    fun setLoginMember() {
        val token = authService.signIn(LoginMemberRequestDto("user1@test.com", "password"))
        val authentication = jwtTokenProvider.getAuthentication(token.accessToken)
        SecurityContextHolder.getContext().authentication = authentication
    }


    @Test
    @Transactional
    @DisplayName("게시글 생성")
    fun createPost() {
//        for (i: Int in 1..20) {
//            if (i % 2 == 0) {
//                val postDto = PostRequestDto(null, "post_title_$i", "post_content_$i", 1, 1, 0)
//                postService.createPost(postDto)
//            } else {
//                val postDto = PostRequestDto(null, "post_title_$i", "post_content_$i", 2, 1, 0)
//                postService.createPost(postDto)
//            }
//        }

        val postDto = PostRequestDto(
            null,
            "post_title_1",
            "post_content_1",
            1,
            35,
            0
        )
        val createdPost = postService.createPost(postDto)

        Assertions.assertThat(createdPost!!.title).isEqualTo("post_title_1")
        Assertions.assertThat(createdPost.content).isEqualTo("post_content_1")
        Assertions.assertThat(createdPost.board.id).isEqualTo(1)
        Assertions.assertThat(createdPost.viewCount).isEqualTo(0)
    }

    @Test
    @Transactional
    @DisplayName("게시글 조회")
    fun getPost() {

        val postDto = PostRequestDto(
            null,
            "post_title_1",
            "post_content_1",
            1,
            1,
            0)
        val createdPost = postService.createPost(postDto)

        val foundPost = postService.getPost(createdPost!!.id)

        Assertions.assertThat(foundPost!!.title).isEqualTo("post_title_1")
        Assertions.assertThat(foundPost.content).isEqualTo("post_content_1")
        Assertions.assertThat(foundPost.board.id).isEqualTo(1)
        Assertions.assertThat(foundPost.viewCount).isEqualTo(0)

    }

    @Test
    @Transactional
    @DisplayName("게시글 목록 조회")
    fun getAllPostsByBoardId() {

        val postDto = PostRequestDto(
            null,
            "post_title_1",
            "post_content_1",
            1,
            1,
            0)
        val foundPosts = postService.getAllPostsByBoardId(postDto)

        Assertions.assertThat(foundPosts!!.size).isEqualTo(0)

    }

    @Test
    @Transactional
    @DisplayName("게시글 수정")
    fun updatePost() {

        val postDto = PostRequestDto(
            null,
            "post_title_1",
            "post_content_1",
            1,
            0,
            0)
        val createdPost = postService.createPost(postDto)

        val foundPost = postService.getPost(createdPost!!.id)

        val updatedPost = postService.updatePost(
            foundPost!!.id,
            PostRequestDto(
                foundPost.id,
                "post_title_2",
                "post_content_2",
                1,
                1,
                0)
        )

        Assertions.assertThat(updatedPost!!.title).isEqualTo("post_title_2")
        Assertions.assertThat(updatedPost.content).isEqualTo("post_content_2")

    }


    @Test
    @DisplayName("게시글 목록 조회")
    fun getPostsByBoardId() {
        val postDto = PostRequestDto(
            null,
            null,
            null,
            1,
            0,
            0
        )

        val pageRequestDto = PageRequestDto(1, 10)

        val foundPosts = postService.getPostsByBoardId(postDto.boardId, pageRequestDto)

        Assertions.assertThat(foundPosts!!.size).isEqualTo(10)

    }
}
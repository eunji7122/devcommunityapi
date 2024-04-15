package com.study.devcommunityapi.domain.post.controller

import com.study.devcommunityapi.common.dto.BaseResponseDto
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import com.study.devcommunityapi.domain.post.service.PostService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping("/list")
    fun getAllPostsByBoardId(@RequestBody postRequestDto: PostRequestDto): BaseResponseDto<List<PostResponseDto>> {
        val posts = postService.getAllPostsByBoardId(postRequestDto)
        return BaseResponseDto(data = posts)
    }

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): BaseResponseDto<PostResponseDto> {
        val post = postService.getPost(id) ?: return BaseResponseDto(message = "삭제된 게시글입니다.")
        return BaseResponseDto(data = post)
    }

    @PostMapping("/")
    fun createPost(@RequestBody postRequestDto: PostRequestDto): BaseResponseDto<PostResponseDto> {
        val createdPost = postService.createPost(postRequestDto)
        return BaseResponseDto(data = createdPost)
    }

    @PutMapping("/{id}")
    fun updatePost(@PathVariable id: Long, @RequestBody postRequestDto: PostRequestDto): BaseResponseDto<PostResponseDto> {
        val updatedPost = postService.updatePost(id, postRequestDto)
        return BaseResponseDto(data = updatedPost)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) {
        postService.deleteBoard(id)
    }

}
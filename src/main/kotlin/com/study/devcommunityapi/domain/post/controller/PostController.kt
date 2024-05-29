package com.study.devcommunityapi.domain.post.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import com.study.devcommunityapi.domain.post.service.PostService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
) {
    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): BaseResponseDto<PostResponseDto>? {
        val post = postService.getPost(id)
        return BaseResponseDto(data = post)
    }

    @PostMapping("/")
    fun createPost(@RequestPart postRequestDto: PostRequestDto, @RequestPart files: List<MultipartFile>?): BaseResponseDto<PostResponseDto> {
        val createdPost = postService.createPost(postRequestDto, files)
        return BaseResponseDto(data = createdPost)
    }

    @PutMapping("/{id}")
    fun updatePost(@PathVariable id: Long, @RequestPart postRequestDto: PostRequestDto, @RequestPart files: List<MultipartFile>?): BaseResponseDto<PostResponseDto> {
        val updatedPost = postService.updatePost(id, postRequestDto, files)
        return BaseResponseDto(data = updatedPost)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long) {
        postService.deletePost(id)
    }

    @PostMapping("/{postId}/heart")
    fun savePostHeart(@PathVariable postId: Long): BaseResponseDto<Any> {
        postService.savePostHeart(postId)
        return BaseResponseDto()
    }

    @DeleteMapping("/{postId}/heart")
    fun deletePostHeart(@PathVariable postId: Long): BaseResponseDto<Any> {
        postService.deletePostHeart(postId)
        return BaseResponseDto()
    }

    @PostMapping("/file")
    fun uploadFile(@RequestPart files: List<MultipartFile>?): BaseResponseDto<List<String>> {
        val uploadedFiles = postService.uploadFile(files)
        return BaseResponseDto(data = uploadedFiles)
    }

    @DeleteMapping("/file")
    fun deleteFile(@RequestPart files: List<MultipartFile>?) {
        postService.deleteFile(files)
    }
}
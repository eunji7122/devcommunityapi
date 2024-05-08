package com.study.devcommunityapi.domain.comment.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.comment.dto.CommentRequestDto
import com.study.devcommunityapi.domain.comment.dto.CommentResponseDto
import com.study.devcommunityapi.domain.comment.service.CommentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comments/")
    fun createComment(@RequestBody commentRequestDto: CommentRequestDto): BaseResponseDto<CommentResponseDto> {
        val createdComment = commentService.createComment(commentRequestDto)
        return BaseResponseDto(createdComment)
    }

    @GetMapping("/posts/{postId}/comments")
    fun getCommentsByBoardId(@PathVariable postId: Long): BaseResponseDto<List<CommentResponseDto>> {
        val comments = commentService.getCommentsByPostId(postId)
        return BaseResponseDto(comments)
    }

    @GetMapping("/comments/{commentId}")
    fun getComment(@PathVariable commentId: Long): BaseResponseDto<CommentResponseDto> {
        val comment = commentService.getComment(commentId)
        return BaseResponseDto(comment)
    }

    @PutMapping("/comments/{commentId}")
    fun updateComment(@PathVariable commentId: Long, @RequestBody commentRequestDto: CommentRequestDto): BaseResponseDto<CommentResponseDto> {
        val comment = commentService.updateComment(commentRequestDto)
        return BaseResponseDto(comment)
    }

    @DeleteMapping("/comments/{commentId}")
    fun deletePost(@PathVariable commentId: Long) {
        commentService.deleteComment(commentId)
    }

    @PostMapping("/comments/{commentId}/heart")
    fun saveCommentHeart(@PathVariable commentId: Long): BaseResponseDto<Any> {
        commentService.saveCommentHeart(commentId)
        return BaseResponseDto()
    }

    @DeleteMapping("/comments/{commentId}/heart")
    fun deleteCommentHeart(@PathVariable commentId: Long): BaseResponseDto<Any> {
        commentService.deleteCommentHeart(commentId)
        return BaseResponseDto()
    }

}
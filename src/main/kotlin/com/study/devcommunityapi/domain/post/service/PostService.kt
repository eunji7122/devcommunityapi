package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.domain.board.repository.BoardRepository
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import com.study.devcommunityapi.domain.post.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val boardRepository: BoardRepository,
) {

    fun createPost(postRequestDto: PostRequestDto) : PostResponseDto? {
        val findBoard = boardRepository.findByIdOrNull(postRequestDto.boardId)
        return postRepository.save(postRequestDto.toEntity(findBoard!!)).toResponseDto()
    }

    fun getPost(id: Long) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(id)
        if (foundPost?.deletedAt != null) {
            return null
        }
        return foundPost?.toResponseDto()
    }

    fun getAllPostsByBoardId(postRequestDto: PostRequestDto) : List<PostResponseDto>? {
        return postRepository.findAllByBoardId(postRequestDto.boardId).stream().map {
            it.toResponseDto()
        }.toList()
    }

    fun updatePost(id: Long, postRequestDto: PostRequestDto) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(id)

        if (foundPost != null) {
            foundPost.title = postRequestDto.title
            foundPost.content = postRequestDto.content

            postRepository.save(foundPost)
            return foundPost.toResponseDto()
        }

        return null
    }

    fun deleteBoard(id: Long) {
        val foundPost = postRepository.findByIdOrNull(id)
        if (foundPost != null) {
            postRepository.delete(foundPost)
        }
    }

}
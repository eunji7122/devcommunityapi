package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.common.util.dto.PageResponseDto
import com.study.devcommunityapi.domain.board.repository.BoardRepository
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import com.study.devcommunityapi.domain.post.entity.Post
import com.study.devcommunityapi.domain.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
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

    fun getPostsByPageRequest(postRequestDto: PostRequestDto): PageResponseDto<PostResponseDto>? {
        val pageable: Pageable = PageRequest.of(
            postRequestDto.pageRequestDto.page - 1,
            postRequestDto.pageRequestDto.size,
            Sort.by("id").descending()
        )

        val posts: Page<Post> = postRepository.findAllByBoardId(postRequestDto.boardId, pageable)

        val dtoList: List<PostResponseDto> = posts.get().map { it.toResponseDto() }.toList()

        return PageResponseDto(
            dtoList,
            posts.pageable.pageNumber + 1,
            posts.pageable.pageSize,
            posts.totalElements,
            posts.totalPages
        )
    }

}
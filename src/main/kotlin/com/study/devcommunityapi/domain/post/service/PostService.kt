package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.common.exception.NotFoundMemberException
import com.study.devcommunityapi.common.exception.NotFoundPostException
import com.study.devcommunityapi.common.util.dto.CustomUser
import com.study.devcommunityapi.common.util.dto.PageRequestDto
import com.study.devcommunityapi.common.util.dto.PageResponseDto
import com.study.devcommunityapi.domain.board.service.BoardService
import com.study.devcommunityapi.domain.member.service.MemberService
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
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val boardService: BoardService,
    private val memberService: MemberService,
) {

    fun createPost(postRequestDto: PostRequestDto) : PostResponseDto? {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundMemberException()
        val foundBoard = boardService.getBoardEntity(postRequestDto.boardId)
        val foundMember = memberService.findMemberByEmail(username)
        return postRepository.save(postRequestDto.toEntity(foundBoard, foundMember)).toResponseDto()
    }

    fun getPost(id: Long) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(id) ?: throw NotFoundPostException()
        return foundPost.toResponseDto()
    }

    fun getPostEntity(id: Long): Post {
        return postRepository.findByIdOrNull(id) ?: throw NotFoundPostException()
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

    fun getPostsByBoardId(boardId: Long, pageRequestDto: PageRequestDto): PageResponseDto<PostResponseDto>? {
        val pageable: Pageable = PageRequest.of(
            pageRequestDto.page - 1,
            pageRequestDto.size,
            Sort.by("id").descending()
        )

        val posts: Page<Post> = postRepository.findAllByBoardId(boardId, pageable)

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
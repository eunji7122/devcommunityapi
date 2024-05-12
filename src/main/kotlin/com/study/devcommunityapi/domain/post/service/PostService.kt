package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.common.exception.NotFoundAuthenticMemberException
import com.study.devcommunityapi.common.exception.NotFoundPostException
import com.study.devcommunityapi.common.util.dto.CustomUser
import com.study.devcommunityapi.common.util.dto.PageRequestDto
import com.study.devcommunityapi.common.util.dto.PageResponseDto
import com.study.devcommunityapi.domain.board.service.BoardService
import com.study.devcommunityapi.domain.member.service.MemberService
import com.study.devcommunityapi.domain.post.dto.PostRequestDto
import com.study.devcommunityapi.domain.post.dto.PostResponseDto
import com.study.devcommunityapi.domain.post.entity.*
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
    private val postHeartService: PostHeartService,
    private val tagService: TagService,
    private val postTagMapService: PostTagMapService,
    private val boardService: BoardService,
    private val memberService: MemberService,
) {

    fun createPost(postRequestDto: PostRequestDto) : PostResponseDto? {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        val foundBoard = boardService.getBoardEntity(postRequestDto.boardId)

        val createdPost = postRepository.save(postRequestDto.toEntity(foundBoard, foundMember))

        if (postRequestDto.tags != "") {
            val tags = tagService.convertToNameListFromTagString(postRequestDto.tags, createdPost.id!!)
            createTags(tags, createdPost)
            return createdPost.toResponseDto(tags = tags)
        }

        return createdPost.toResponseDto()
    }

    fun savePost(post: Post): Post {
        return postRepository.save(post)
    }

    fun getPost(postId: Long) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(postId) ?: throw NotFoundPostException()
        val heartCount = postHeartService.getHeartCountByPost(postId)
        val tags = postTagMapService.getTagsByPost(foundPost.id!!)
        return foundPost.toResponseDto(heartCount, tagService.convertToNameList(tags!!))
    }

    fun getPostEntity(id: Long): Post {
        return postRepository.findByIdOrNull(id) ?: throw NotFoundPostException()
    }

    fun getPostsByBoardId(boardId: Long, pageRequestDto: PageRequestDto): PageResponseDto<PostResponseDto> {
        val pageable: Pageable = PageRequest.of(
            pageRequestDto.page - 1,
            pageRequestDto.size,
            Sort.by(pageRequestDto.orderCriteria).descending()
        )

        val posts: Page<PostWithHeartCount> = when (pageRequestDto.orderCriteria) {
            "viewCount" -> {
                postRepository.findAllByBoardIdWithHeartCountOrderByViews(boardId, pageable)
            }
            "heartCount" -> {
                postRepository.findAllByBoardIdWithHeartCountOrderByHeartCount(boardId, pageable)
            }
            else -> {
                postRepository.findAllByBoardIdWithHeartCountOrderByNewest(boardId, pageable)
            }
        }

        val postIdList: List<Long> = posts.get().map { it.toPostIdDto() }.toList()

        val tagList: List<PostTagMap> = postTagMapService.getAllByPostIdList(postIdList)

        val dtoList: List<PostResponseDto> = posts.get().map { item ->
            val tags = tagList.filter { subItem -> subItem.post.id == item.post.id }.map { it.tag.name }
            item.toResponseDto(tags)
        }.toList()

        return PageResponseDto(
            dtoList,
            posts.pageable.pageNumber + 1,
            posts.pageable.pageSize,
            posts.totalElements,
            posts.totalPages
        )
    }

    fun updatePost(id: Long, postRequestDto: PostRequestDto) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(id)

        if (foundPost != null) {
            foundPost.title = postRequestDto.title
            foundPost.content = postRequestDto.content

            val foundTags = postTagMapService.getTagsByPost(postRequestDto.id!!)
            val newTags = tagService.convertToNameListFromTagString(postRequestDto.tags, postRequestDto.id)

            // 게시글에 등록되어있던 tag 일괄 삭제
            postTagMapService.deletePostTagMaps(postRequestDto.id, foundTags!!)
            createTags(newTags, foundPost)

            postRepository.save(foundPost)

            return foundPost.toResponseDto(tags = newTags)
        }

        return null
    }

    fun deletePost(postId: Long) {
        val foundPost = postRepository.findByIdOrNull(postId)
        if (foundPost != null) {
            val foundTags = postTagMapService.getTagsByPost(postId)
            postTagMapService.deletePostTagMaps(postId, foundTags!!)

            postRepository.delete(foundPost)
        }
    }

    fun savePostHeart(postId: Long) {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        val foundPost = postRepository.findByIdOrNull(postId) ?: throw NotFoundPostException()

        postHeartService.savePostHeart(foundPost, foundMember)
    }

    fun deletePostHeart(postId: Long) {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        postHeartService.deletePostHeart(postId, foundMember.id!!)
    }

    private fun createTags(tags: List<String>, post: Post) {
        // tag 리스트를 순회하면서 저장 (post 당 tag는 여러개임)
        for (tag in tags) {
            // 태그 생성
            val createdTag = tagService.saveTag(tag)

            if (createdTag != null) {
                // 새 tag라면 postTagMap에 생성된 새 tag 저장
                postTagMapService.savePostTagMap(post, createdTag)
            } else {
                // 이미 존재하는 tag라면 해당 tag를 찾아서 저장
                val findTag = tagService.getTag(tag)
                if (findTag != null) {
                    postTagMapService.savePostTagMap(post, findTag)
                }
            }
        }
    }

}
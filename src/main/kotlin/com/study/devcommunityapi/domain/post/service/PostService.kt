package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.common.aws.AwsS3Service
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
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val postHeartService: PostHeartService,
    private val tagService: TagService,
    private val postTagMapService: PostTagMapService,
    private val boardService: BoardService,
    private val memberService: MemberService,
    private val awsS3Service: AwsS3Service,
) {

    fun createPost(postRequestDto: PostRequestDto, files: List<MultipartFile>?) : PostResponseDto? {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()
        val foundMember = memberService.findMemberByEmail(username)
        val foundBoard = boardService.getBoardEntity(postRequestDto.boardId)

        val uploadedFiles = awsS3Service.uploadFiles(files)

        val createdPost = postRepository.save(postRequestDto.toEntity(foundBoard, foundMember, convertStringToPostImage(uploadedFiles)))

        if (postRequestDto.tags != "") {
            val tags = tagService.convertToNameListFromTagString(postRequestDto.tags!!, createdPost.id!!)
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
        increaseViewCount(foundPost)
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
                if (pageRequestDto.searchKeyword.isEmpty())
                    postRepository.findAllByBoardIdWithHeartCountOrderByViews(boardId, pageable)
                else
                    postRepository.findAllByBoardIdWithHeartCountOrderByViews(boardId, pageRequestDto.searchKeyword, pageable)
            }
            "heartCount" -> {
                if (pageRequestDto.searchKeyword.isEmpty())
                    postRepository.findAllByBoardIdWithHeartCountOrderByHeartCount(boardId, pageable)
                else
                    postRepository.findAllByBoardIdWithHeartCountOrderByHeartCount(boardId, pageRequestDto.searchKeyword, pageable)

            }
            else -> {
                if (pageRequestDto.searchKeyword.isEmpty())
                    postRepository.findAllByBoardIdWithHeartCountOrderByNewest(boardId, pageable)
                else
                    postRepository.findAllByBoardIdWithHeartCountOrderByNewest(boardId, pageRequestDto.searchKeyword, pageable)
            }
        }

        val postIdList: List<Long> = posts.get().map { it.toPostIdDto() }.toList()

        val tagList: List<PostTagMap> = postTagMapService.getAllByPostIdList(postIdList)

        val dtoList: List<PostResponseDto> = posts.get().map { item ->
            val tags = tagList.filter { subItem -> subItem.post.id == item.post.id }.map { it.tag.name }
            item.toResponseDto(tags, item.post.images)
        }.toList()

        return PageResponseDto(
            dtoList,
            posts.pageable.pageNumber + 1,
            posts.pageable.pageSize,
            posts.totalElements,
            posts.totalPages,
            pageRequestDto.searchKeyword
        )
    }

    fun updatePost(id: Long, postRequestDto: PostRequestDto, files: List<MultipartFile>?) : PostResponseDto? {
        val foundPost = postRepository.findByIdOrNull(id) ?: throw NotFoundPostException()

        foundPost.title = postRequestDto.title
        foundPost.content = postRequestDto.content

        val foundTags = postTagMapService.getTagsByPost(foundPost.id!!)
        val newTags = tagService.convertToNameListFromTagString(postRequestDto.tags!!, foundPost.id)

        // 게시글에 등록되어있던 tag 일괄 삭제
        postTagMapService.deletePostTagMaps(foundPost.id, foundTags!!)
        createTags(newTags, foundPost)

        // 이미지 수정
        awsS3Service.deleteFile(foundPost.images.map { it.imageUrl })
        if (files != null) {
            val uploadedFiles = awsS3Service.uploadFiles(files)
            foundPost.images = convertStringToPostImage(uploadedFiles)
        } else {
            foundPost.images = arrayListOf()
        }

        postRepository.save(foundPost)

        return foundPost.toResponseDto(tags = newTags)
    }

    fun deletePost(postId: Long) {
        val foundPost = postRepository.findByIdOrNull(postId)
        if (foundPost != null) {
            val foundTags = postTagMapService.getTagsByPost(postId)
            postTagMapService.deletePostTagMaps(postId, foundTags!!)
            awsS3Service.deleteFile(foundPost.images.map { it.imageUrl })

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

    fun increaseViewCount(post: Post): Post {
        // 조회수 증가
        post.viewCount += 1
        return postRepository.save(post)
    }

    fun uploadFile(files: List<MultipartFile>?) : List<String>? {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()

        return awsS3Service.uploadFiles(files)
    }

    fun deleteFile(files: List<MultipartFile>?) {
        val username = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
            ?: throw NotFoundAuthenticMemberException()
        awsS3Service.deleteFile(files?.map { it.name })
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

    private fun convertStringToPostImage(files: List<String>): List<PostImage> {
        val postImages = arrayListOf<PostImage>()

        for (file in files) {
            postImages.add(PostImage(file))
        }

        return postImages
    }

}
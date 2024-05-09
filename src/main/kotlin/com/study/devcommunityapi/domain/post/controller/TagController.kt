package com.study.devcommunityapi.domain.post.controller

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import com.study.devcommunityapi.domain.post.service.PostTagMapService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tags")
class TagController (
    private val postTagMapService: PostTagMapService
) {

    @GetMapping("/most")
    fun getTagsSortByMost(): BaseResponseDto<List<Map<String, Long>>> {
        val tags = postTagMapService.getPostTagMapsSortByMost()
        return BaseResponseDto(data = tags)
    }
}
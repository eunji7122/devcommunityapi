package com.study.devcommunityapi.domain.post.service

import com.study.devcommunityapi.domain.post.entity.PostTagMap
import com.study.devcommunityapi.domain.post.entity.Tag
import com.study.devcommunityapi.domain.post.repository.TagRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
@Transactional
class TagService (
    private val tagRepository: TagRepository,
) {

    fun convertToNameListFromTagString(tagString: String, postId: Long): List<String> {
        val pattern = Pattern.compile("#(\\S+)")
        val matcher = pattern.matcher(tagString)

        val tags = arrayListOf<String>()

        while (matcher.find()) {
            tags.add(matcher.group(1))
        }

        return tags
    }

    fun convertToNameList(postTagMap: List<PostTagMap>): List<String> {
        val result = arrayListOf<String>()
        for (map in postTagMap) {
            result.add(map.tag.name)
        }
        return result
    }

    fun saveTag(tagName: String): Tag? {
        if (tagRepository.findByName(tagName) == null) {
            return tagRepository.save(Tag(tagName))
        }
        return null
    }

    fun getTag(tagName: String): Tag? {
        return tagRepository.findByName(tagName)
    }

    fun deleteTag(tagName: String) {
        tagRepository.deleteByName(tagName)
    }



}
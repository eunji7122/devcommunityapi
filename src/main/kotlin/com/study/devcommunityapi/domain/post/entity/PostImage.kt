package com.study.devcommunityapi.domain.post.entity

import jakarta.persistence.Embeddable

@Embeddable
class PostImage (
    val imageUrl: String
)
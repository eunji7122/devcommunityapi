package com.study.devcommunityapi.domain.auth.entity

import com.study.devcommunityapi.common.util.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table
class RefreshToken(

    @Id
    @Column(nullable = false, unique = true, length = 30, updatable = false)
    var id: String, // username(=email)

    @Column(nullable = false)
    var refreshToken: String,

) : BaseEntity()
package com.study.devcommunityapi.common.util.dto

import org.springframework.http.HttpStatus


data class BaseResponseDto<T>(

    // 조회시 데이터를 담아서 반환해줄 data
    val data: T? = null,

    val status: HttpStatus = HttpStatus.OK,

    val message: String? = null,

)

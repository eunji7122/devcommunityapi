package com.study.devcommunityapi.common.util.dto

import com.study.devcommunityapi.common.util.status.ResultCode

data class BaseResponseDto<T>(

    // 결과 코드
    val resultCode: String = ResultCode.SUCCESS.name,

    // 조회시 데이터를 담아서 반환해줄 data
    val data: T? = null,

    // 처리 메시지
    val message: String = ResultCode.SUCCESS.msg,

)

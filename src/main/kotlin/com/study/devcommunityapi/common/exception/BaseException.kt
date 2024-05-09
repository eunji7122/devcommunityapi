package com.study.devcommunityapi.common.exception

import org.springframework.http.HttpStatus

sealed class BaseException(
    val code: Int,
    val status: HttpStatus,
    override val message: String
) : Exception(message)

class BadRequestException : BaseException(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
class IllegalArgumentException : BaseException(400, HttpStatus.BAD_REQUEST, "올바르지 않은 인자가 있습니다.")
class BadCredentialsException: BaseException(400, HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호를 다시 확인하세요.")
class UnauthorizedException: BaseException(401, HttpStatus.UNAUTHORIZED, "인증 권한이 없습니다")
class JwtExpiredException: BaseException(403, HttpStatus.FORBIDDEN, "토큰이 만료되었습니다. 다시 로그인 하세요")
class JwtMalformedException: BaseException(403, HttpStatus.FORBIDDEN, "잘못된 형식의 토큰입니다.")
class NotFoundAuthenticMemberException: BaseException(404, HttpStatus.NOT_FOUND, "로그인 정보를 찾을 수 없습니다.")
class NotFoundMemberException: BaseException(404, HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.")
class NotFoundBoardException: BaseException(404, HttpStatus.NOT_FOUND, "존재하지 않는 게시판입니다.")
class NotFoundPostException: BaseException(404, HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다.")
class NotFoundCommentException: BaseException(404, HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.")
class NotFoundTokenException: BaseException(404, HttpStatus.NOT_FOUND, "존재하지 않는 토큰입니다.")
class ConflictMemberException : BaseException(409, HttpStatus.CONFLICT, "이미 등록된 아이디입니다.")
class InternalServerErrorException: BaseException(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다")
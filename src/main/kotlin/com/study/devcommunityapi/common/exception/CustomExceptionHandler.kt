package com.study.devcommunityapi.common.exception

import com.study.devcommunityapi.common.util.dto.BaseResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    // 그 외 모든 exception (기본 예외)
    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponseDto<Any>> {
        return ResponseEntity(
            BaseResponseDto(
                status = HttpStatus.BAD_REQUEST,
                message = ex.message
            ), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BaseException::class)
    protected fun baseException(ex: BaseException): ResponseEntity<BaseResponseDto<Any>> {
        return ResponseEntity(BaseResponseDto(
            status = ex.status,
            message = ex.message
        ), ex.status)
    }

    // @Valid에서 걸린 경우 발생
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponseDto<Any>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.getDefaultMessage()
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        return ResponseEntity(BaseResponseDto(status = HttpStatus.BAD_REQUEST, message = ex.message), HttpStatus.BAD_REQUEST)
    }

}
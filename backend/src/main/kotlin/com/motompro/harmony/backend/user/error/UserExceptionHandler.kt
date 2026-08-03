package com.motompro.harmony.backend.user.error

import com.motompro.harmony.backend.error.ErrorResponse
import com.motompro.harmony.backend.user.exception.EmailAlreadyExistsException
import com.motompro.harmony.backend.user.exception.NameAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.motompro.harmony.backend.user"])
class UserExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailAlreadyExists(ex: EmailAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(
            message = "Email already exists",
            code = UserErrorCode.EMAIL_ALREADY_EXISTS)
        )
    }

    @ExceptionHandler(NameAlreadyExistsException::class)
    fun handleNameAlreadyExists(ex: NameAlreadyExistsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(
            message = "Name already exists",
            code = UserErrorCode.NAME_ALREADY_EXISTS)
        )
    }
}
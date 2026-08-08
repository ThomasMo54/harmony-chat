package com.motompro.harmony.backend.auth.error

import com.motompro.harmony.backend.auth.exception.InvalidCredentialsException
import com.motompro.harmony.backend.auth.exception.UserNotEnabledException
import com.motompro.harmony.backend.error.ErrorResponse
import com.motompro.harmony.backend.user.exception.ActivationCodeExpiredException
import com.motompro.harmony.backend.user.exception.ActivationCodeNotFoundException
import com.motompro.harmony.backend.user.exception.EmailAlreadyExistsException
import com.motompro.harmony.backend.user.exception.NameAlreadyExistsException
import com.motompro.harmony.backend.user.exception.UserAlreadyActivatedException
import com.motompro.harmony.backend.user.exception.UserWithIdNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.motompro.harmony.backend.auth"])
class AuthExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(ex: InvalidCredentialsException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(
            message = ex.message,
            code = AuthErrorCode.INVALID_CREDENTIALS
        ))
    }

    @ExceptionHandler(UserNotEnabledException::class)
    fun handleUserNotEnabled(ex: UserNotEnabledException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(
            message = ex.message,
            code = AuthErrorCode.USER_NOT_ENABLED
        ))
    }
}
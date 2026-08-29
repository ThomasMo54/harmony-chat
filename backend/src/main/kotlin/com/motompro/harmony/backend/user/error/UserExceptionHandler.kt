package com.motompro.harmony.backend.user.error

import com.motompro.harmony.backend.error.ErrorResponse
import com.motompro.harmony.backend.user.exception.ActivationCodeExpiredException
import com.motompro.harmony.backend.user.exception.ActivationCodeNotFoundException
import com.motompro.harmony.backend.user.exception.EmailAlreadyExistsException
import com.motompro.harmony.backend.user.exception.NameAlreadyExistsException
import com.motompro.harmony.backend.user.exception.UserAlreadyActivatedException
import com.motompro.harmony.backend.user.exception.UserWithEmailNotFoundException
import com.motompro.harmony.backend.user.exception.UserWithIdNotFoundException
import com.motompro.harmony.backend.user.exception.UserWithNameNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMalformedUserInput(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(
            message = "Malformed input",
        ))
    }

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

    @ExceptionHandler(UserWithIdNotFoundException::class)
    fun handleUserWithIdNotFound(ex: UserWithIdNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.NOT_FOUND
        ))
    }

    @ExceptionHandler(UserWithEmailNotFoundException::class)
    fun handleUserWithEmailNotFound(ex: UserWithEmailNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.NOT_FOUND
        ))
    }

    @ExceptionHandler(UserWithNameNotFoundException::class)
    fun handleUserWithNameNotFound(ex: UserWithNameNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.NOT_FOUND
        ))
    }

    @ExceptionHandler(ActivationCodeNotFoundException::class)
    fun handleActivationCodeNotFound(ex: ActivationCodeNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.ACTIVATION_CODE_NOT_FOUND
        ))
    }

    @ExceptionHandler(ActivationCodeExpiredException::class)
    fun handleActivationCodeExpired(ex: ActivationCodeExpiredException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.GONE).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.ACTIVATION_CODE_EXPIRED
        ))
    }

    @ExceptionHandler(UserAlreadyActivatedException::class)
    fun handleUserAlreadyActivated(ex: UserAlreadyActivatedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(
            message = ex.message,
            code = UserErrorCode.ALREADY_ACTIVATED
        ))
    }
}
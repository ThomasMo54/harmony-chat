package com.motompro.harmony.backend.contact.error

import com.motompro.harmony.backend.contact.exception.ContactRequestAlreadySentException
import com.motompro.harmony.backend.contact.exception.ContactRequestNotFoundException
import com.motompro.harmony.backend.contact.exception.ContactRequestToSelfException
import com.motompro.harmony.backend.contact.exception.UserAlreadyInContactException
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

@RestControllerAdvice(basePackages = ["com.motompro.harmony.backend.contact"])
class ContactExceptionHandler {

    @ExceptionHandler(ContactRequestAlreadySentException::class)
    fun handleContactRequestAlreadySent(ex: ContactRequestAlreadySentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse(
            code = ContactErrorCode.REQUEST_ALREADY_SENT,
            message = ex.message,
        ))
    }

    @ExceptionHandler(UserAlreadyInContactException::class)
    fun handleUserAlreadyInContact(ex: UserAlreadyInContactException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(
            code = ContactErrorCode.ALREADY_IN_CONTACT,
            message = ex.message,
        ))
    }

    @ExceptionHandler(ContactRequestToSelfException::class)
    fun handleContactRequestToSelf(ex: ContactRequestToSelfException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(
            code = ContactErrorCode.REQUEST_TO_SELF,
            message = ex.message,
        ))
    }

    @ExceptionHandler(ContactRequestNotFoundException::class)
    fun handleContactRequestNotFound(ex: ContactRequestNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(
            code = ContactErrorCode.REQUEST_NOT_FOUND,
            message = ex.message,
        ))
    }
}
package com.motompro.harmony.backend.user.controller

import com.motompro.harmony.backend.interceptor.ratelimit.RateLimit
import com.motompro.harmony.backend.user.service.UserNotificationService
import com.motompro.harmony.backend.user.service.UserService
import com.motompro.harmony.backend.user.dto.ActivateUserDto
import com.motompro.harmony.backend.user.dto.CreateUserDto
import com.motompro.harmony.backend.user.dto.PublicUserDto
import com.motompro.harmony.backend.user.dto.ResendCodeDto
import com.motompro.harmony.backend.user.dto.UserDto
import com.motompro.harmony.backend.user.exception.ActivationCodeExpiredException
import com.motompro.harmony.backend.user.exception.ActivationCodeNotFoundException
import com.motompro.harmony.backend.user.exception.UserAlreadyActivatedException
import com.motompro.harmony.backend.user.exception.UserWithIdNotFoundException
import com.motompro.harmony.backend.user.mapper.toDto
import com.motompro.harmony.backend.user.mapper.toPublicDto
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userNotificationService: UserNotificationService,
) {

    @RateLimit(capacity = 5, refillTokens = 5, refillDuration = 1, refillUnit = ChronoUnit.MINUTES)
    @PostMapping
    fun createUser(@Valid @RequestBody createUserDto: CreateUserDto): ResponseEntity<UserDto> {
        val (user, activationCode) = userService.createUserAndActivationCode(createUserDto)
        userNotificationService.sendWelcomeEmail(user, activationCode.code, UserService.VALIDATION_CODE_EXPIRATION_TIME)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toDto())
    }

    @GetMapping("/search")
    fun searchUsers(
        @RequestParam query: String,
        @PageableDefault(size = 20)
        pageable: Pageable
    ): Page<PublicUserDto> {
        return userService.searchUsersByName(query, pageable).map { it.toPublicDto() }
    }

    @RateLimit(capacity = 1, refillTokens = 1, refillDuration = 1, refillUnit = ChronoUnit.MINUTES)
    @PostMapping("/resend-code")
    fun resendCode(@Valid @RequestBody resendCodeDto: ResendCodeDto): ResponseEntity<*> {
        val userId = resendCodeDto.userId
        val user = userService.findUserById(userId)
            ?: throw UserWithIdNotFoundException(userId)
        if (user.isEnabled) {
            throw UserAlreadyActivatedException()
        }
        userService.deleteUserActivationCodes(userId)
        val activationCode = userService.createActivationCode(userId)
        userNotificationService.sendWelcomeEmail(user, activationCode.code, UserService.VALIDATION_CODE_EXPIRATION_TIME)
        return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    }

    @RateLimit(capacity = 5, refillTokens = 5, refillDuration = 1, refillUnit = ChronoUnit.MINUTES)
    @PatchMapping("/activate")
    fun activateUser(@Valid @RequestBody activateUserDto: ActivateUserDto) {
        val activationCode = userService.findUserActivationCodeByCode(activateUserDto.code)
            ?: throw ActivationCodeNotFoundException(activateUserDto.code)
        if (activationCode.expiresAt.isBefore(OffsetDateTime.now())) {
            throw ActivationCodeExpiredException()
        }
        userService.activateUser(activationCode.userId)
        userService.deleteUserActivationCodes(activationCode.userId)
    }
}
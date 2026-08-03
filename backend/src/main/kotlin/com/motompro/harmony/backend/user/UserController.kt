package com.motompro.harmony.backend.user

import com.motompro.harmony.backend.error.ErrorResponse
import com.motompro.harmony.backend.user.dto.ActivateUserDto
import com.motompro.harmony.backend.user.dto.CreateUserDto
import com.motompro.harmony.backend.user.dto.ResendCodeDto
import com.motompro.harmony.backend.user.dto.UserDto
import com.motompro.harmony.backend.user.exception.ActivationCodeExpiredException
import com.motompro.harmony.backend.user.exception.ActivationCodeNotFoundException
import com.motompro.harmony.backend.user.exception.UserAlreadyActivatedException
import com.motompro.harmony.backend.user.exception.UserNotFoundException
import com.motompro.harmony.backend.user.mapper.toDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userNotificationService: UserNotificationService,
) {

    @PostMapping
    fun createUser(@Valid @RequestBody createUserDto: CreateUserDto): ResponseEntity<UserDto> {
        val (user, activationCode) = userService.createUserAndActivationCode(createUserDto)
        userNotificationService.sendWelcomeEmail(user, activationCode.code, UserService.VALIDATION_CODE_EXPIRATION_TIME)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toDto())
    }

    @PostMapping("/resend-code")
    fun resendCode(@Valid @RequestBody resendCodeDto: ResendCodeDto): ResponseEntity<*> {
        val userId = resendCodeDto.userId
        val user = userService.findUserById(userId)
            ?: throw UserNotFoundException(userId)
        if (user.isEnabled) {
            throw UserAlreadyActivatedException()
        }
        userService.deleteUserActivationCodes(userId)
        val activationCode = userService.createActivationCode(userId)
        userNotificationService.sendWelcomeEmail(user, activationCode.code, UserService.VALIDATION_CODE_EXPIRATION_TIME)
        return ResponseEntity.status(HttpStatus.CREATED).build<Any>()
    }

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
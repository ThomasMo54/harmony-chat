package com.motompro.harmony.backend.user.dto

import com.motompro.harmony.backend.user.service.UserService
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ActivateUserDto(
    @field:NotBlank
    @field:Size(min = UserService.VALIDATION_CODE_LENGTH, max = UserService.VALIDATION_CODE_LENGTH)
    val code: String,
)
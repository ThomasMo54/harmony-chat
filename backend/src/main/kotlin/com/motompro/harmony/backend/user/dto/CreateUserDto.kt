package com.motompro.harmony.backend.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserDto(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 200)
    val password: String,

    @field:NotBlank
    @field:Size(min = 3, max = 30)
    var name: String,
)
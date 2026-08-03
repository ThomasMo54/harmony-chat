package com.motompro.harmony.backend.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class UserDto(
    val id: UUID,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(max = 30)
    val name: String,
)
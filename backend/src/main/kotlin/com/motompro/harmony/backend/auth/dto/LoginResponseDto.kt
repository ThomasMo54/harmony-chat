package com.motompro.harmony.backend.auth.dto

import jakarta.validation.constraints.NotBlank

data class LoginResponseDto(
    @field:NotBlank
    val token: String,
)
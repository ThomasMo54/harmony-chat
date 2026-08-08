package com.motompro.harmony.backend.auth.dto

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestDto(
    @field:NotBlank
    val refreshToken: String,
)
package com.motompro.harmony.backend.auth.dto

import jakarta.validation.constraints.NotBlank

data class RefreshTokenResponseDto(
    @field:NotBlank
    val accessToken: String,

    @field:NotBlank
    val refreshToken: String
)
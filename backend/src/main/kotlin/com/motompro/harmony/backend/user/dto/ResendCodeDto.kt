package com.motompro.harmony.backend.user.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class ResendCodeDto(
    @field:NotBlank
    val userId: UUID,
)
package com.motompro.harmony.backend.error

data class ErrorResponse(
    val message: String? = null,
    val code: String? = ErrorCode.GENERIC
)
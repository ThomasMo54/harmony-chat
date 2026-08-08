package com.motompro.harmony.backend.auth.error

import com.motompro.harmony.backend.error.ErrorCode

object AuthErrorCode {

    const val PREFIX = "${ErrorCode.PREFIX}.auth"

    const val INVALID_CREDENTIALS = "${PREFIX}.invalid_credentials"
    const val USER_NOT_ENABLED = "${PREFIX}.user_not_enabled"
}
package com.motompro.harmony.backend.auth.error

import com.motompro.harmony.backend.error.ErrorCode

object AuthErrorCode {

    const val PREFIX = "${ErrorCode.PREFIX}.auth"

    const val INVALID_CREDENTIALS = "${PREFIX}.invalid_credentials"
    const val USER_NOT_ENABLED = "${PREFIX}.user_not_enabled"

    const val INVALID_REFRESH_TOKEN = "${PREFIX}.invalid_refresh_token"
    const val EXPIRED_REFRESH_TOKEN = "${PREFIX}.expired_refresh_token"
    const val REFRESH_TOKEN_REUSE = "${PREFIX}.refresh_token_reuse"
}
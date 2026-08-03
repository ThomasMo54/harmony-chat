package com.motompro.harmony.backend.user.error

import com.motompro.harmony.backend.error.ErrorCode

object UserErrorCode {

    const val PREFIX = "${ErrorCode.PREFIX}.user"

    const val EMAIL_ALREADY_EXISTS = "${PREFIX}.email_already_exists"
    const val NAME_ALREADY_EXISTS = "${PREFIX}.name_already_exists"
}
package com.motompro.harmony.backend.user.error

import com.motompro.harmony.backend.error.ErrorCode

object UserErrorCode {

    const val PREFIX = "${ErrorCode.PREFIX}.user"

    const val EMAIL_ALREADY_EXISTS = "${PREFIX}.email_already_exists"
    const val NAME_ALREADY_EXISTS = "${PREFIX}.name_already_exists"

    const val NOT_FOUND = "${PREFIX}.not_found"

    const val ACTIVATION_CODE_NOT_FOUND = "${PREFIX}.code.not_found"
    const val ACTIVATION_CODE_EXPIRED = "${PREFIX}.code.expired"
    const val ALREADY_ACTIVATED = "${PREFIX}.already_activated"
}
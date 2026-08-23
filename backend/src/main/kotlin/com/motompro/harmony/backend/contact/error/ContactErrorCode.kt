package com.motompro.harmony.backend.contact.error

import com.motompro.harmony.backend.error.ErrorCode

object ContactErrorCode {

    const val PREFIX = "${ErrorCode.PREFIX}.contact"

    const val REQUEST_ALREADY_SENT = "${PREFIX}.request.already_sent"
    const val ALREADY_IN_CONTACT = "${PREFIX}.request.already_in_contact"
    const val REQUEST_TO_SELF = "${PREFIX}.request.self"
    const val REQUEST_NOT_FOUND = "${PREFIX}.request.not_found"
}
package com.motompro.harmony.backend.contact.dto

import java.time.OffsetDateTime
import java.util.UUID

data class ContactSummaryDto(
    val userId: UUID,
    val userName: String,
    val date: OffsetDateTime,
)
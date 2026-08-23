package com.motompro.harmony.backend.contact.dto

import java.time.OffsetDateTime
import java.util.UUID

data class ContactRequestDto(
    val requesterId: UUID,
    val requestedId: UUID,
    val createdAt: OffsetDateTime,
)
package com.motompro.harmony.backend.contact.mapper

import com.motompro.harmony.backend.contact.dto.ContactRequestDto
import com.motompro.harmony.backend.contact.entity.ContactRequest



fun ContactRequest.toDto(): ContactRequestDto = ContactRequestDto(
    requesterId = this.id.requesterId,
    requestedId = this.id.requestedId,
    createdAt = createdAt,
)
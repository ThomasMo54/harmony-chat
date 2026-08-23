package com.motompro.harmony.backend.contact.repository

import com.motompro.harmony.backend.contact.entity.ContactRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface ContactRequestRepository : JpaRepository<ContactRequest, ContactRequest.ContactRequestId> {

    fun findById_RequesterId(
        idRequestedId: UUID,
        pageable: Pageable
    ): Page<ContactRequest>

    fun findById_RequestedId(
        idRequestedId: UUID,
        pageable: Pageable
    ): Page<ContactRequest>

    fun findById_RequesterIdAndId_RequestedId(idRequesterId: UUID, idRequestedId: UUID): Optional<ContactRequest>

    fun existsById_RequesterIdAndId_RequestedId(idRequesterId: UUID, idRequestedId: UUID): Boolean
}
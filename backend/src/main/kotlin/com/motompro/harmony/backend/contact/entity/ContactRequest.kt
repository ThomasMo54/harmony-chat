package com.motompro.harmony.backend.contact.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "contact_requests")
class ContactRequest(
    @EmbeddedId
    val id: ContactRequestId,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now()
) {

    @Embeddable
    data class ContactRequestId(
        val requesterId: UUID,
        val requestedId: UUID
    ) : Serializable
}
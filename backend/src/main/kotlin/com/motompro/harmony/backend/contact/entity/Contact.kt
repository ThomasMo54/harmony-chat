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
@Table(name = "contacts")
class Contact(
    @EmbeddedId
    val id: ContactId,

    @Column(name = "date", nullable = false)
    val date: OffsetDateTime = OffsetDateTime.now()
) {

    fun otherUserId(currentUserId: UUID): UUID {
        return if (id.userId1 == currentUserId) id.userId2 else id.userId1
    }

    @Embeddable
    data class ContactId(
        @Column(name = "user_id_1", nullable = false)
        val userId1: UUID,

        @Column(name = "user_id_2", nullable = false)
        val userId2: UUID
    ) : Serializable
}
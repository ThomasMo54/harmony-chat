package com.motompro.harmony.backend.contact.repository

import com.motompro.harmony.backend.contact.entity.Contact
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ContactRepository : JpaRepository<Contact, Contact.ContactId> {

    fun findAllById_UserId1OrId_UserId2(
        idUserId1: UUID,
        idUserId2: UUID,
        pageable: Pageable
    ): Page<Contact>

    fun existsAllById_UserId1AndId_UserId2(
        idUserId1: UUID,
        idUserId2: UUID
    ): Boolean
}
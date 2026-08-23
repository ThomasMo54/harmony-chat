package com.motompro.harmony.backend.contact.service

import com.motompro.harmony.backend.contact.entity.Contact
import com.motompro.harmony.backend.contact.entity.ContactRequest
import com.motompro.harmony.backend.contact.exception.ContactRequestAlreadySentException
import com.motompro.harmony.backend.contact.exception.ContactRequestNotFoundException
import com.motompro.harmony.backend.contact.exception.ContactRequestToSelfException
import com.motompro.harmony.backend.contact.exception.UserAlreadyInContactException
import com.motompro.harmony.backend.contact.repository.ContactRepository
import com.motompro.harmony.backend.contact.repository.ContactRequestRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val contactRequestRepository: ContactRequestRepository,
) {

    fun createContactRequest(requesterId: UUID, requestedId: UUID) {
        if (requesterId == requestedId) {
            throw ContactRequestToSelfException()
        }
        val (id1, id2) = normalizeIds(requesterId, requestedId)
        if (contactRepository.existsAllById_UserId1AndId_UserId2(id1, id2)) {
            throw UserAlreadyInContactException(requestedId)
        }
        if (contactRequestRepository.existsById_RequesterIdAndId_RequestedId(requesterId, requestedId)) {
            throw ContactRequestAlreadySentException(requestedId)
        }
        val contactRequest = ContactRequest(ContactRequest.ContactRequestId(requesterId, requestedId), OffsetDateTime.now())
        contactRequestRepository.saveAndFlush(contactRequest)
    }

    fun findContacts(userId: UUID, pageable: Pageable): Page<Contact> {
        return contactRepository.findAllById_UserId1OrId_UserId2(userId, userId, pageable)
    }

    fun findContactRequestsByRequester(requesterId: UUID, pageable: Pageable): Page<ContactRequest> {
        return contactRequestRepository.findById_RequesterId(requesterId, pageable)
    }

    fun findContactRequestsByRequested(requestedId: UUID, pageable: Pageable): Page<ContactRequest> {
        return contactRequestRepository.findById_RequestedId(requestedId, pageable)
    }

    @Transactional
    fun acceptContactRequest(requesterId: UUID, requestedId: UUID) {
        val request = contactRequestRepository.findById_RequesterIdAndId_RequestedId(requesterId, requestedId)
        if (request.isEmpty) {
            throw ContactRequestNotFoundException(requesterId, requestedId)
        }
        val (id1, id2) = normalizeIds(requesterId, requestedId)
        val contact = Contact(Contact.ContactId(id1, id2), OffsetDateTime.now())
        contactRepository.save(contact)
        contactRequestRepository.delete(request.get())
    }

    fun rejectContactRequest(requesterId: UUID, requestedId: UUID) {
        val request = contactRequestRepository.findById_RequesterIdAndId_RequestedId(requesterId, requestedId)
        if (request.isEmpty) {
            throw ContactRequestNotFoundException(requesterId, requestedId)
        }
        contactRequestRepository.delete(request.get())
    }

    private fun normalizeIds(id1: UUID, id2: UUID): Pair<UUID, UUID> {
        return if (id1 < id2) id1 to id2 else id2 to id1
    }
}
package com.motompro.harmony.backend.contact.controller

import com.motompro.harmony.backend.auth.UserPrincipal
import com.motompro.harmony.backend.contact.dto.ContactSummaryDto
import com.motompro.harmony.backend.contact.dto.ContactRequestAnswerDto
import com.motompro.harmony.backend.contact.dto.ContactRequestDto
import com.motompro.harmony.backend.contact.dto.CreateContactRequestDto
import com.motompro.harmony.backend.contact.mapper.toDto
import com.motompro.harmony.backend.contact.service.ContactService
import com.motompro.harmony.backend.user.service.UserService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/contacts")
class ContactController(
    private val contactService: ContactService,
    private val userService: UserService,
) {

    @PostMapping("/requests")
    fun requestContact(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody createContactRequestDto: CreateContactRequestDto,
    ) {
        contactService.createContactRequest(principal.getId(), createContactRequestDto.requestedId)
    }

    @GetMapping
    fun findContacts(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PageableDefault(size = 20, sort = ["date"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): Page<ContactSummaryDto> {
        val userId = principal.getId()
        val contactsPage = contactService.findContacts(userId, pageable)

        val otherUserIds = contactsPage.content.map { it.otherUserId(userId) }

        val publicUsersById = userService.findPublicUsersByIdIn(otherUserIds).associateBy { it.id }

        val dtos = contactsPage.content.map { contact ->
            val otherUserId = contact.otherUserId(userId)
            val publicUser = publicUsersById[otherUserId]!!
            ContactSummaryDto(publicUser.id, publicUser.name, contact.date)
        }

        return PageImpl(dtos, contactsPage.pageable, contactsPage.totalElements)
    }

    @GetMapping("/requests/sent")
    fun findSentContactRequests(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): Page<ContactRequestDto> {
        return contactService.findContactRequestsByRequester(principal.getId(), pageable)
            .map { it.toDto() }
    }

    @GetMapping("/requests/received")
    fun findReceivedContactRequests(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable
    ): Page<ContactRequestDto> {
        return contactService.findContactRequestsByRequested(principal.getId(), pageable)
            .map { it.toDto() }
    }

    @PutMapping("/requests/accept")
    fun acceptContactRequest(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody contactRequestAnswerDto: ContactRequestAnswerDto,
    ) {
        contactService.acceptContactRequest(contactRequestAnswerDto.requesterId, principal.getId())
    }

    @PutMapping("/requests/reject")
    fun rejectContactRequest(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody contactRequestAnswerDto: ContactRequestAnswerDto,
    ) {
        contactService.rejectContactRequest(contactRequestAnswerDto.requesterId, principal.getId())
    }
}
package com.motompro.harmony.backend.contact.exception

import java.util.UUID

class ContactRequestNotFoundException(requesterId: UUID, requestedId: UUID) : RuntimeException("Contact request from $requesterId to $requestedId not found")
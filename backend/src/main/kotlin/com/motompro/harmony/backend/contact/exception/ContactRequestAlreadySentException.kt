package com.motompro.harmony.backend.contact.exception

import java.util.UUID

class ContactRequestAlreadySentException(requestedId: UUID) : RuntimeException("A request has already been sent to $requestedId")
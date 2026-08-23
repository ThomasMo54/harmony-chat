package com.motompro.harmony.backend.contact.exception

import java.util.UUID

class UserAlreadyInContactException(requestedId: UUID) : RuntimeException("User $requestedId is already in your contacts")
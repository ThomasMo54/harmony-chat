package com.motompro.harmony.backend.user.exception

import java.util.UUID

class UserNotFoundException(id: UUID) : RuntimeException("User with $id not found")
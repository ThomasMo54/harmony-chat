package com.motompro.harmony.backend.user.exception

import java.util.UUID

class UserWithIdNotFoundException(id: UUID) : RuntimeException("User with id $id not found")
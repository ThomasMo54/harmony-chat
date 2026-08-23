package com.motompro.harmony.backend.user.entity

import java.util.UUID

interface PublicUser {
    val id: UUID
    val name: String
}
package com.motompro.harmony.backend.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "user_activation_codes")
class UserActivationCode(
    @Id
    val code: String,

    @Column(name = "user_id")
    val userId: UUID,

    @Column(name = "expires_at")
    val expiresAt: OffsetDateTime,
)

package com.motompro.harmony.backend.auth.repository

import com.motompro.harmony.backend.auth.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime
import java.util.Optional
import java.util.UUID

interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {

    fun findByToken(token: String): Optional<RefreshToken>

    fun deleteAllByUserId(userId: UUID)

    fun findAllByUserIdAndRevokedFalse(userId: UUID): List<RefreshToken>

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now OR t.revoked = true")
    fun deleteAllExpiredOrRevoked(now: OffsetDateTime)
}
package com.motompro.harmony.backend.auth.service

import com.motompro.harmony.backend.auth.entity.RefreshToken
import com.motompro.harmony.backend.auth.exception.InvalidRefreshTokenException
import com.motompro.harmony.backend.auth.exception.RefreshTokenExpiredException
import com.motompro.harmony.backend.auth.exception.RefreshTokenReuseDetectedException
import com.motompro.harmony.backend.auth.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.OffsetDateTime
import java.util.Base64
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${jwt.refresh-token-expiration-ms}") private val refreshTokenExpirationMs: Long
) {

    private val secureRandom = SecureRandom()

    @Transactional
    fun createRefreshToken(userId: UUID): String {
        val rawToken = generateRawToken()
        val tokenHash = hashToken(rawToken)

        val refreshToken = RefreshToken(
            userId = userId,
            token = tokenHash,
            expiresAt = OffsetDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000)
        )
        refreshTokenRepository.save(refreshToken)

        return rawToken
    }

    @Transactional
    fun rotateRefreshToken(rawToken: String): Pair<UUID, String> {
        val tokenHash = hashToken(rawToken)
        val existing = refreshTokenRepository.findByToken(tokenHash)
            .orElseThrow { InvalidRefreshTokenException() }

        if (existing.revoked) {
            refreshTokenRepository.deleteAllByUserId(existing.userId)
            throw RefreshTokenReuseDetectedException()
        }

        if (existing.expiresAt.isBefore(OffsetDateTime.now())) {
            throw RefreshTokenExpiredException()
        }

        existing.revoked = true
        refreshTokenRepository.save(existing)

        val newRawToken = createRefreshToken(existing.userId)
        return existing.userId to newRawToken
    }

    @Transactional
    fun revokeAllForUser(userId: UUID) {
        refreshTokenRepository.deleteAllByUserId(userId)
    }

    private fun generateRawToken(): String {
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    private fun hashToken(rawToken: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(rawToken.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}
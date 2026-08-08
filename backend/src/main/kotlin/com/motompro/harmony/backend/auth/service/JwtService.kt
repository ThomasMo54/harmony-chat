package com.motompro.harmony.backend.auth.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtService(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-expiration-ms}") private val expirationMs: Long
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateAccessToken(userId: UUID): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    fun extractUserId(token: String): UUID {
        return UUID.fromString(parseClaims(token).subject)
    }

    fun isTokenValid(token: String, userId: UUID): Boolean {
        val extractedId = extractUserId(token)
        return extractedId == userId && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return parseClaims(token).expiration.before(Date())
    }

    private fun parseClaims(token: String) =
        Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
}
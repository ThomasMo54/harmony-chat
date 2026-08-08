package com.motompro.harmony.backend.auth.task

import com.motompro.harmony.backend.auth.repository.RefreshTokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Component
class RefreshTokenCleanupTask(
    private val refreshTokenRepository: RefreshTokenRepository,
) {

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    fun cleanupExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredOrRevoked(OffsetDateTime.now())
    }
}
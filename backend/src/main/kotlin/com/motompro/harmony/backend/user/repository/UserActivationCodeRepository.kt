package com.motompro.harmony.backend.user.repository

import com.motompro.harmony.backend.user.entity.UserActivationCode
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserActivationCodeRepository : JpaRepository<UserActivationCode, String> {

    fun deleteAllByUserId(userId: UUID)
}
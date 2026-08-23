package com.motompro.harmony.backend.user.repository

import com.motompro.harmony.backend.user.entity.PublicUser
import com.motompro.harmony.backend.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {

    fun findPublicUsersByIdIn(ids: Collection<UUID>): List<PublicUser>

    fun findByEmail(email: String): Optional<User>

    fun findAllByNameContainingIgnoreCase(fragment: String, pageable: Pageable): Page<User>
    fun id(id: UUID): MutableList<User>
}
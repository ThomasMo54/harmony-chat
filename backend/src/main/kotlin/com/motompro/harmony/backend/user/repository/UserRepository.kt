package com.motompro.harmony.backend.user.repository

import com.motompro.harmony.backend.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID>
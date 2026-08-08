package com.motompro.harmony.backend.auth

import com.motompro.harmony.backend.user.exception.UserWithEmailNotFoundException
import com.motompro.harmony.backend.user.exception.UserWithIdNotFoundException
import com.motompro.harmony.backend.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserPrincipal {
        val user = userRepository.findByEmail(email)
            .orElseThrow { UserWithEmailNotFoundException(email) }
        return UserPrincipal(user)
    }

    fun loadUserById(id: UUID): UserPrincipal {
        val user = userRepository.findById(id)
            .orElseThrow { UserWithIdNotFoundException(id) }
        return UserPrincipal(user)
    }
}
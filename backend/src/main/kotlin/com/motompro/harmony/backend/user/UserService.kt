package com.motompro.harmony.backend.user

import com.motompro.harmony.backend.user.dto.CreateUserDto
import com.motompro.harmony.backend.user.exception.EmailAlreadyExistsException
import com.motompro.harmony.backend.user.exception.NameAlreadyExistsException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun createUser(createUserDto: CreateUserDto) {
        val user = createUserDto.toEntity(passwordEncoder)
        try {
            userRepository.save(user)
        } catch (ex: DataIntegrityViolationException) {
            val constraintName = (ex.cause as? ConstraintViolationException)?.constraintName
            throw when (constraintName) {
                UNIQUE_EMAIL_CONSTRAINT -> EmailAlreadyExistsException()
                UNIQUE_NAME_CONSTRAINT -> NameAlreadyExistsException()
                else -> ex
            }
        }
    }

    companion object {
        private const val UNIQUE_EMAIL_CONSTRAINT = "uq_users_email"
        private const val UNIQUE_NAME_CONSTRAINT = "uq_users_name"
    }
}
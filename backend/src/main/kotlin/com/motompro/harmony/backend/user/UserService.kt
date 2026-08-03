package com.motompro.harmony.backend.user

import com.motompro.harmony.backend.user.dto.CreateUserDto
import com.motompro.harmony.backend.user.entity.User
import com.motompro.harmony.backend.user.entity.UserActivationCode
import com.motompro.harmony.backend.user.exception.EmailAlreadyExistsException
import com.motompro.harmony.backend.user.exception.NameAlreadyExistsException
import com.motompro.harmony.backend.user.exception.UserNotFoundException
import com.motompro.harmony.backend.user.mapper.toEntity
import com.motompro.harmony.backend.user.repository.UserRepository
import com.motompro.harmony.backend.user.repository.UserActivationCodeRepository
import jakarta.persistence.EntityNotFoundException
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.random.Random

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userActivationCodeRepository: UserActivationCodeRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun createUserAndActivationCode(createUserDto: CreateUserDto): Pair<User, UserActivationCode> {
        val user = createUser(createUserDto)
        val code = createActivationCode(user.id)
        return user to code
    }

    fun createActivationCode(userId: UUID): UserActivationCode {
        val codeBuilder = StringBuilder()
        repeat(VALIDATION_CODE_LENGTH) { codeBuilder.append(Random.nextInt(10)) }
        val code = codeBuilder.toString()
        val expiresAt = OffsetDateTime.now().plusMinutes(VALIDATION_CODE_EXPIRATION_TIME.toLong())
        return userActivationCodeRepository.save(UserActivationCode(
            userId = userId,
            code = code,
            expiresAt = expiresAt,
        ))
    }

    fun findUserById(id: UUID): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun findUserActivationCodeByCode(code: String): UserActivationCode? {
        return userActivationCodeRepository.findById(code).orElse(null)
    }

    fun activateUser(userId: UUID) {
        val user = findUserById(userId)
            ?: throw UserNotFoundException(userId)
        user.isEnabled = true
        userRepository.save(user)
    }

    @Transactional
    fun deleteUserActivationCodes(userId: UUID) {
        userActivationCodeRepository.deleteAllByUserId(userId)
    }

    private fun createUser(createUserDto: CreateUserDto): User {
        val user = createUserDto.toEntity(passwordEncoder)
        try {
            return userRepository.save(user)
        } catch (ex: DataIntegrityViolationException) {
            val constraintName = (ex.cause as? ConstraintViolationException)?.constraintName
            throw when (constraintName) {
                UNIQUE_EMAIL_CONSTRAINT -> EmailAlreadyExistsException(createUserDto.email)
                UNIQUE_NAME_CONSTRAINT -> NameAlreadyExistsException(createUserDto.name)
                else -> ex
            }
        }
    }

    companion object {
        private const val UNIQUE_EMAIL_CONSTRAINT = "uq_users_email"
        private const val UNIQUE_NAME_CONSTRAINT = "uq_users_name"

        const val VALIDATION_CODE_LENGTH = 6
        const val VALIDATION_CODE_EXPIRATION_TIME = 15 // Minutes
    }
}
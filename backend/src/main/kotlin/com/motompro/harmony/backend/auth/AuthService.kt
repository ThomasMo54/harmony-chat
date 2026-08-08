package com.motompro.harmony.backend.auth

import com.motompro.harmony.backend.auth.dto.LoginRequestDto
import com.motompro.harmony.backend.auth.dto.LoginResponseDto
import com.motompro.harmony.backend.auth.exception.InvalidCredentialsException
import com.motompro.harmony.backend.auth.exception.UserNotEnabledException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val jwtService: JwtService
) {

    fun login(request: LoginRequestDto): LoginResponseDto {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (_: DisabledException) {
            throw UserNotEnabledException()
        } catch (_: AuthenticationException) {
            throw InvalidCredentialsException()
        }

        val userPrincipal = userDetailsService.loadUserByUsername(request.email)
        val token = jwtService.generateToken(userPrincipal.getId())
        return LoginResponseDto(token)
    }
}
package com.motompro.harmony.backend.auth.service

import com.motompro.harmony.backend.auth.CustomUserDetailsService
import com.motompro.harmony.backend.auth.dto.LoginRequestDto
import com.motompro.harmony.backend.auth.dto.LoginResponseDto
import com.motompro.harmony.backend.auth.dto.RefreshTokenRequestDto
import com.motompro.harmony.backend.auth.dto.RefreshTokenResponseDto
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
    private val jwtService: JwtService,
    private val refreshTokenService: RefreshTokenService
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
        val accessToken = jwtService.generateAccessToken(userPrincipal.getId())
        val refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getId())

        return LoginResponseDto(accessToken, refreshToken)
    }

    fun refresh(request: RefreshTokenRequestDto): RefreshTokenResponseDto {
        val (userId, newRefreshToken) = refreshTokenService.rotateRefreshToken(request.refreshToken)
        val newAccessToken = jwtService.generateAccessToken(userId)
        return RefreshTokenResponseDto(newAccessToken, newRefreshToken)
    }

    fun logout(refreshToken: String) {
        val userId = try {
            refreshTokenService.rotateRefreshToken(refreshToken).first
        } catch (_: Exception) {
            return
        }
        refreshTokenService.revokeAllForUser(userId)
    }
}
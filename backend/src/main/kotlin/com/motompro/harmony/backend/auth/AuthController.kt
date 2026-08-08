package com.motompro.harmony.backend.auth

import com.motompro.harmony.backend.auth.dto.LoginRequestDto
import com.motompro.harmony.backend.auth.dto.LoginResponseDto
import com.motompro.harmony.backend.auth.dto.RefreshTokenRequestDto
import com.motompro.harmony.backend.auth.dto.RefreshTokenResponseDto
import com.motompro.harmony.backend.auth.service.AuthService
import com.motompro.harmony.backend.interceptor.ratelimit.RateLimit
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {

    @RateLimit(capacity = 5, refillTokens = 5, refillDuration = 1, refillUnit = ChronoUnit.MINUTES)
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequestDto): LoginResponseDto =
        authService.login(request)

    @RateLimit(capacity = 5, refillTokens = 5, refillDuration = 1, refillUnit = ChronoUnit.MINUTES)
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequestDto): RefreshTokenResponseDto =
        authService.refresh(request)

    @PostMapping("/logout")
    fun logout(@Valid @RequestBody request: RefreshTokenRequestDto) {
        authService.logout(request.refreshToken)
    }
}
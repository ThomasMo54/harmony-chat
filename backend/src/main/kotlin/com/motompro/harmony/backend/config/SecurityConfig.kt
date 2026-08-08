package com.motompro.harmony.backend.config

import com.motompro.harmony.backend.auth.CustomUserDetailsService
import com.motompro.harmony.backend.auth.JwtService
import com.motompro.harmony.backend.filter.JwtAuthenticationFilter
import com.motompro.harmony.backend.filter.RateLimitingFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/auth/login", permitAll)
                authorize(HttpMethod.POST, "/users", permitAll)
                authorize("/users/resend-code", permitAll)
                authorize("/users/activate", permitAll)
                authorize(anyRequest, authenticated)
            }
            csrf { disable() }
            formLogin { disable() }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
        }

        http.addFilterBefore(RateLimitingFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtService, userDetailsService),
            UsernamePasswordAuthenticationFilter::class.java
        )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
package com.motompro.harmony.backend.user

import com.motompro.harmony.backend.user.dto.CreateUserDto
import org.springframework.security.crypto.password.PasswordEncoder

fun CreateUserDto.toEntity(passwordEncoder: PasswordEncoder): User = User(
    email = this.email,
    password = passwordEncoder.encode(this.password)!!,
    name = this.name,
)

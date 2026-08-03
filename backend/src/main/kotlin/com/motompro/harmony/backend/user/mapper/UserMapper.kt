package com.motompro.harmony.backend.user.mapper

import com.motompro.harmony.backend.user.dto.CreateUserDto
import com.motompro.harmony.backend.user.dto.UserDto
import com.motompro.harmony.backend.user.entity.User
import org.springframework.security.crypto.password.PasswordEncoder

fun CreateUserDto.toEntity(passwordEncoder: PasswordEncoder): User = User(
    email = this.email,
    password = passwordEncoder.encode(this.password)!!,
    name = this.name,
)

fun User.toDto(): UserDto = UserDto(
    id = this.id,
    email = this.email,
    name = this.name,
)

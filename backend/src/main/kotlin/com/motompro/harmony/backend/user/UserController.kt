package com.motompro.harmony.backend.user

import com.motompro.harmony.backend.user.dto.CreateUserDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {

    @PostMapping
    fun createUser(@Valid @RequestBody createUserDto: CreateUserDto) {
        userService.createUser(createUserDto)
    }
}
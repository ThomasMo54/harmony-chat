package com.motompro.harmony.backend.user.exception

class UserWithEmailNotFoundException(email: String) : RuntimeException("User with email $email not found")
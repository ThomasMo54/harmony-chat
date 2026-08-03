package com.motompro.harmony.backend.user.exception

class EmailAlreadyExistsException(email: String) : RuntimeException("User with email $email already exists")
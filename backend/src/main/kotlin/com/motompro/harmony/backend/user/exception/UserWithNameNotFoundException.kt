package com.motompro.harmony.backend.user.exception

class UserWithNameNotFoundException(name: String) : RuntimeException("User with name $name not found")
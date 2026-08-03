package com.motompro.harmony.backend.user.exception

class NameAlreadyExistsException(name: String) : RuntimeException("User with name $name already exists")
package com.motompro.harmony.backend.user.exception

class ActivationCodeNotFoundException(code: String) : RuntimeException("Activation code $code not found")
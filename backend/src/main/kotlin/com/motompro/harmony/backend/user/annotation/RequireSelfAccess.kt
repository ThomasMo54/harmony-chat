package com.motompro.harmony.backend.user.annotation

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("#id == authentication.principal.getId()")
annotation class RequireSelfAccess

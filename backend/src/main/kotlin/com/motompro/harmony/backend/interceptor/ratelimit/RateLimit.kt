package com.motompro.harmony.backend.interceptor.ratelimit

import java.time.temporal.ChronoUnit

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RateLimit(
    val capacity: Long = 20,
    val refillTokens: Long = 20,
    val refillDuration: Long = 1,
    val refillUnit: ChronoUnit = ChronoUnit.MINUTES,
    val disabled: Boolean = false,
)

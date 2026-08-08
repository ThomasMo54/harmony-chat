package com.motompro.harmony.backend.interceptor.ratelimit

import io.github.bucket4j.Bucket
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Component
class RateLimitInterceptor : HandlerInterceptor {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) return true

        val rateLimit = handler.getMethodAnnotation(RateLimit::class.java)
            ?: handler.beanType.getAnnotation(RateLimit::class.java)
            ?: RateLimit()

        if (rateLimit.disabled) return true

        val ip = getClientIp(request)
        val bucketKey = "$ip:${handler.method.name}"

        val bucket = buckets.computeIfAbsent(bucketKey) { newBucket(rateLimit) }
        val probe = bucket.tryConsumeAndReturnRemaining(1)

        response.setHeader("X-RateLimit-Remaining", probe.remainingTokens.toString())

        if (probe.isConsumed) {
            return true
        }

        val waitSeconds = probe.nanosToWaitForRefill / 1_000_000_000
        response.setHeader("X-RateLimit-Retry-After-Seconds", waitSeconds.toString())
        response.status = 429
        response.contentType = "application/json"
        response.writer.write("""{"message": "Too many requests, try again in ${waitSeconds}s"}""")
        return false
    }

    private fun newBucket(rateLimit: RateLimit): Bucket {
        val duration = Duration.of(rateLimit.refillDuration, rateLimit.refillUnit)
        return Bucket.builder()
            .addLimit { limit -> limit.capacity(rateLimit.capacity).refillGreedy(rateLimit.refillTokens, duration) }
            .build()
    }

    private fun getClientIp(request: HttpServletRequest): String {
        return request.remoteAddr
    }
}
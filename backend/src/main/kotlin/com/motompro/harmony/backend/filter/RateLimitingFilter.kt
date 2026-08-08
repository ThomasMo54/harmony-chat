package com.motompro.harmony.backend.filter

import io.github.bucket4j.Bucket
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class RateLimitingFilter : OncePerRequestFilter() {

    private val buckets = ConcurrentHashMap<String, Bucket>()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val ip = getClientIp(request)
        val bucket = buckets.computeIfAbsent(ip) { newBucket() }
        val probe = bucket.tryConsumeAndReturnRemaining(1)

        response.setHeader("X-RateLimit-Remaining", probe.remainingTokens.toString())

        if (probe.isConsumed) {
            filterChain.doFilter(request, response)
        } else {
            val waitSeconds = probe.nanosToWaitForRefill / 1_000_000_000
            response.setHeader("X-RateLimit-Retry-After-Seconds", waitSeconds.toString())
            response.status = 429
            response.contentType = "application/json"
            response.writer.write("""{"message": "Too many requests, try again in ${waitSeconds}s"}""")
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.startsWith("/actuator/")
    }

    private fun newBucket(): Bucket {
        return Bucket.builder()
            .addLimit { limit -> limit.capacity(100).refillGreedy(100, Duration.ofMinutes(1)) }
            .build()
    }

    private fun getClientIp(request: HttpServletRequest): String {
        return request.getHeader("X-Forwarded-For")?.split(",")?.first()?.trim()
            ?: request.remoteAddr
    }
}
package com.sar.shopaholism.data.web.ratelimiting

import com.sar.shopaholism.data.web.ratelimiting.model.RateLimit
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateLimiterTest {

    private lateinit var sut: RateLimiter

    @Test
    fun `WHEN rate limit is 20 requests per minute AND limit is equal to requests sent RETURN isRateLimited true`() {
        val rateLimits = listOf(
            RateLimit(
                maxRequestsWithinTimeSpan = 20,
                timeSpanInSeconds = 60,
                requestsSentWithinTimeSpan = 19
            )
        )
        sut = RateLimiter(rateLimits)

        sut.requestSent()

        assertTrue(sut.isRateLimited())
    }

    @Test
    fun `WHEN rate limit is 20 requests per minute AND limit is exceeded to requests sent RETURN isRateLimited true`() {
        val rateLimits = listOf(
            RateLimit(
                maxRequestsWithinTimeSpan = 20,
                timeSpanInSeconds = 60,
                requestsSentWithinTimeSpan = 20
            )
        )
        sut = RateLimiter(rateLimits)

        sut.requestSent()

        assertEquals(21, rateLimits.first().requestsSentWithinTimeSpan)
        assertTrue(sut.isRateLimited())
    }

    @Test
    fun `WHEN rate limit is 18 requests per minute AND request limit is not reached RETURN false`() {
        val rateLimits = listOf(
            RateLimit(
                maxRequestsWithinTimeSpan = 20,
                timeSpanInSeconds = 60,
                requestsSentWithinTimeSpan = 18
            )
        )
        sut = RateLimiter(rateLimits)

        sut.requestSent()

        assertFalse(sut.isRateLimited())
    }

    @Test
    fun `WHEN request limit is hit outside of timeSpan THEN update timeSpanStart RETURN isRateLimited false`() {
        val pastTimestamp = Instant.now().minusSeconds(61)
        val rateLimit = RateLimit(
            maxRequestsWithinTimeSpan = 20,
            timeSpanInSeconds = 60,
            requestsSentWithinTimeSpan = 19,
            pastTimestamp
        )

        val rateLimits = listOf(
            rateLimit
        )
        sut = RateLimiter(rateLimits)

        sut.requestSent()

        assertTrue(pastTimestamp.isBefore(rateLimit.timeSpanStartTimestamp))
        assertEquals(1, rateLimit.requestsSentWithinTimeSpan)
        assertFalse(sut.isRateLimited())
    }
}
package com.sar.shopaholism.data.remote.ratelimiting

import com.sar.shopaholism.data.remote.ratelimiting.model.RateLimit
import java.time.Instant
import java.time.temporal.ChronoUnit

class RateLimiter(private val rateLimits: List<RateLimit>) {

    private fun RateLimit.isWithinTimeSpan(): Boolean {
        val secondsPassed = ChronoUnit.SECONDS.between(timeSpanStartTimestamp, Instant.now())

        return secondsPassed <= timeSpanInSeconds
    }

    private fun RateLimit.isRequestLimitExceeded() =
        requestsSentWithinTimeSpan >= maxRequestsWithinTimeSpan

    private fun RateLimit.isRateLimited() = isRequestLimitExceeded() && isWithinTimeSpan()

    private fun List<RateLimit>.isAnyRateLimitHit(): Boolean {
        return any { it.isRateLimited() }
    }

    private fun List<RateLimit>.increaseRequestsSent() {
        forEach {
            it.requestsSentWithinTimeSpan++

            if (!it.isWithinTimeSpan()) {
                it.timeSpanStartTimestamp = Instant.now()

                if (it.isRequestLimitExceeded()) {
                    it.requestsSentWithinTimeSpan = 1
                }
            }
        }
    }

    fun isRateLimited() = rateLimits.isAnyRateLimitHit()

    fun requestSent() {
        rateLimits.increaseRequestsSent()
    }
}
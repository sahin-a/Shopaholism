package com.sar.shopaholism.data.remote.productlookup.dao

import com.sar.shopaholism.data.remote.ratelimiting.RateLimiter
import com.sar.shopaholism.data.remote.ratelimiting.model.RateLimit

class BarcodeLookupApiRateLimit {
    val rateLimiter = RateLimiter(
        listOf(
            RateLimit(
                maxRequestsWithinTimeSpan = 50,
                timeSpanInSeconds = 60
            )
        )
    )
}
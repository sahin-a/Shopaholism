package com.sar.shopaholism.data.remote.ratelimiting.model

import java.time.Instant

data class RateLimit(
    val maxRequestsWithinTimeSpan: Int,
    val timeSpanInSeconds: Int,
    var requestsSentWithinTimeSpan: Int = 0,
    var timeSpanStartTimestamp: Instant = Instant.now()
)
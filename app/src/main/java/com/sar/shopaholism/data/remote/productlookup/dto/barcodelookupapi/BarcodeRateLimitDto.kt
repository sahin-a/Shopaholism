package com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi

data class BarcodeRateLimitDto(
    val allowed_calls_per_minute: String,
    val allowed_calls_per_month: String,
    val remaining_calls_per_minute: String,
    val remaining_calls_per_month: String
)
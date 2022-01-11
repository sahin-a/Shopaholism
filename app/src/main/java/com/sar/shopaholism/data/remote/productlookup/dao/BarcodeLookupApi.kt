package com.sar.shopaholism.data.remote.productlookup.dao

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeProductsDto
import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeRateLimitDto
import java.time.Instant
import java.time.ZoneOffset

class RateLimiter {

    private var maxRequestsPerMonth: Int = ZERO_REQUESTS_LEFT
    private var maxRequestsPerMinute: Int = ZERO_REQUESTS_LEFT
    private var requestsLeftPerMonth: Int = ZERO_REQUESTS_LEFT
    private var requestsLeftPerMinute: Int = ZERO_REQUESTS_LEFT

    private var lastRequestSentTimestamp = Instant.now()

    var isInitialized = false
        private set

    fun setData(barcodeRateLimiterDto: BarcodeRateLimitDto) {
        maxRequestsPerMonth = barcodeRateLimiterDto.allowed_calls_per_month.toInt()
        maxRequestsPerMinute = barcodeRateLimiterDto.allowed_calls_per_minute.toInt()

        requestsLeftPerMonth = barcodeRateLimiterDto.remaining_calls_per_month.toInt()
        requestsLeftPerMinute = barcodeRateLimiterDto.remaining_calls_per_minute.toInt()

        isInitialized = true
    }

    fun isRateLimited(): Boolean =
        requestsLeftPerMinute <= ZERO_REQUESTS_LEFT || requestsLeftPerMonth <= ZERO_REQUESTS_LEFT

    fun increaseSentRequests() {
        val currentTimestamp = Instant.now()

        val currentTime = currentTimestamp.atZone(ZoneOffset.UTC)
        val previousTime = lastRequestSentTimestamp.atZone(ZoneOffset.UTC)

        if (requestsLeftPerMonth > ZERO_REQUESTS_LEFT) {

            if (currentTime.hour == previousTime.hour) {

                when (currentTime.minute == previousTime.minute) {
                    true -> requestsLeftPerMinute--
                    else -> requestsLeftPerMinute = maxRequestsPerMinute
                }
            }

        } else {
            requestsLeftPerMinute = ZERO_REQUESTS_LEFT
        }

        lastRequestSentTimestamp = currentTimestamp
    }

    companion object {
        private const val ZERO_REQUESTS_LEFT = 0
    }

}

// https://www.barcodelookup.com/api-documentation
class BarcodeLookupApi(
    private val fuelManager: FuelManager,
    private val rateLimiter: RateLimiter,
    private val apiToken: String = "t4hy31yt18a90aefsdzadq7wkmpx0n"
) {

    // TODO: set geo location param, we don't wanna see us products as european
    // TODO: cache the results, due to limited amount of requests per minute/month
    // TODO: create send response func

    init {
        fuelManager.basePath = "https://api.barcodelookup.com/v3/"
        fuelManager.baseParams = listOf("key" to apiToken)
    }

    private inline fun <reified T : Any> parseResponse(result: Result<String, FuelError>): T {
        val (data, error) = result

        error?.let {
            throw it.exception
        }

        data?.let {
            return Gson().fromJson(it, T::class.java)
        }

        throw Exception("Failed to parse Response")
    }

    private inline fun <reified ResponseDto : Any> sendRequest(
        request: Request,
        checkRateLimit: Boolean
    ): ResponseDto {

        if (!checkRateLimit) {
            return sendRequest(request)
        }

        if (!rateLimiter.isInitialized) {
            rateLimiter.setData(getRateLimit())
        }

        if (rateLimiter.isRateLimited()) {
            throw Exception("Rate limited")
        }

        val (_, _, result) = request.responseString()

        rateLimiter.increaseSentRequests()

        return parseResponse(result)
    }

    private inline fun <reified ResponseDto : Any> sendRequest(
        request: Request
    ): ResponseDto {
        val (_, _, result) = request.responseString()

        return parseResponse(result)
    }

    private fun getRateLimit(): BarcodeRateLimitDto {
        val request = fuelManager.get(
            path = "rate-limits"
        )

        return sendRequest(request, checkRateLimit = false)
    }

    fun getProductsByName(name: String): BarcodeProductsDto {
        val request = fuelManager.get(
            path = "products",
            parameters = listOf("title" to name)
        )

        return sendRequest(request, checkRateLimit = true)
    }

}
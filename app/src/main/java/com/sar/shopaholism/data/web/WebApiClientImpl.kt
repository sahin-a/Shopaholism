package com.sar.shopaholism.data.web

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.coroutines.awaitString
import com.sar.shopaholism.data.web.ratelimiting.RateLimiter
import com.sar.shopaholism.data.web.exceptions.RateLimitedException
import com.sar.shopaholism.data.web.exceptions.WebRequestUnsuccessfulException
import com.sar.shopaholism.data.web.model.Request
import com.sar.shopaholism.data.serizaliation.Deserializer
import com.sar.shopaholism.domain.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WebApiClientImpl(
    private val fuelManager: FuelManager,
    private val logger: Logger,
    private var rateLimiter: RateLimiter? = null
) : WebApiClient {

    override fun setBasePath(basePath: String) {
        fuelManager.basePath = basePath
    }

    override fun setBaseParams(baseParams: Map<String, String>) {
        fuelManager.baseParams = baseParams.map { Pair<String, Any?>(it.key, it.value) }
    }

    fun setRateLimiter(rateLimiter: RateLimiter?) {
        this.rateLimiter = rateLimiter
    }

    private fun Request.toFuelRequest(): com.github.kittinunf.fuel.core.Request =
        fuelManager.request(
            method = Method.valueOf(method.value),
            path = path,
            parameters = parameters.map { Pair<String, Any?>(it.key, it.value) }
        )

    override suspend fun sendRequest(request: Request, dispatcher: CoroutineDispatcher): String =
        withContext(dispatcher) {
            if (rateLimiter?.isRateLimited() == true) {
                throw RateLimitedException("Rate limited")
            }

            try {
                return@withContext request.toFuelRequest().awaitString()
            } catch (e: Exception) {
                throw WebRequestUnsuccessfulException("Web request failed", e)
            }
        }

    override suspend fun <T> sendRequest(
        request: Request,
        deserializer: Deserializer,
        classOfT: Class<T>,
        dispatcher: CoroutineDispatcher
    ): T {
        val response = sendRequest(request, dispatcher)
        return deserializer.deserialize(response, classOfT)
    }
}
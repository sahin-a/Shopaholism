package com.sar.shopaholism.data.remote.productlookup.dao

import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeProductsDto
import com.sar.shopaholism.data.remote.productlookup.exceptions.FailedToRetrieveProductsException
import com.sar.shopaholism.data.remote.web.HttpMethod
import com.sar.shopaholism.data.remote.web.WebApiClient
import com.sar.shopaholism.data.remote.web.exceptions.WebRequestUnsuccessfulException
import com.sar.shopaholism.data.remote.web.model.Request
import com.sar.shopaholism.data.serizaliation.JsonDeserializer

// https://www.barcodelookup.com/api-documentation
class BarcodeLookupApi(
    private val webApiClient: WebApiClient,
    private val rateLimiter: BarcodeLookupApiRateLimit
) {

    init {
        val apiToken = "zx2s0b304pabgka9uosgb1ixq5yexc"
        val basePath = "https://api.barcodelookup.com/v3/"
        val baseParams = mapOf("key" to apiToken)

        webApiClient.apply {
            setBasePath(basePath)
            setBaseParams(baseParams)
        }
    }

    suspend fun getProductsByName(name: String): BarcodeProductsDto {
        val request = Request(
            path = "products",
            method = HttpMethod.GET,
            parameters = mapOf("title" to name)
        )

        try {
            return webApiClient.sendRequest(
                request,
                JsonDeserializer(),
                BarcodeProductsDto::class.java
            )
        } catch (e: WebRequestUnsuccessfulException) {
            throw FailedToRetrieveProductsException("Failed to retrieve products", e)
        }
    }
}
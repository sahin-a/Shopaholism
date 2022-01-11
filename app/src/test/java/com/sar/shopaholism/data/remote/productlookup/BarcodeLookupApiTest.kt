package com.sar.shopaholism.data.remote.productlookup

import com.github.kittinunf.fuel.core.FuelManager
import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.dao.RateLimiter
import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeRateLimitDto
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class BarcodeLookupApiTest {

    private lateinit var fuelManager: FuelManager
    private lateinit var rateLimiter: RateLimiter
    private lateinit var api: BarcodeLookupApi

    @Before
    fun setup() {
        fuelManager = FuelManager()
        rateLimiter = RateLimiter()
        api = BarcodeLookupApi(fuelManager, rateLimiter)
    }

    @Test
    fun `is rate limited when no request per month left`() {
        rateLimiter.setData(
            BarcodeRateLimitDto(
                allowed_calls_per_month = "100",
                allowed_calls_per_minute = "10",
                remaining_calls_per_month = "5",
                remaining_calls_per_minute = "1"
            )
        )

        rateLimiter.increaseSentRequests()

        assertTrue(rateLimiter.isRateLimited())
    }

    @Test
    fun `retrieve products by name`() {
        val title = "Playstation 5"

        val productsDto = api.getProductsByName(title)

        assertTrue(productsDto.barcodeProducts.count() > 0)
    }

}
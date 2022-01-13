package com.sar.shopaholism.data.remote.productlookup

import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApiRateLimit
import com.sar.shopaholism.data.remote.productlookup.exceptions.FailedToRetrieveProductsException
import com.sar.shopaholism.data.remote.ratelimiting.RateLimiter
import com.sar.shopaholism.data.remote.ratelimiting.model.RateLimit
import com.sar.shopaholism.data.remote.web.WebApiClient
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class BarcodeLookupApiTest {

    @MockK
    private lateinit var rateLimiter: BarcodeLookupApiRateLimit

    @MockK
    private lateinit var webApiClient: WebApiClient

    @InjectMockKs
    private lateinit var sut: BarcodeLookupApi

    @Before
    fun setup() {
        every { rateLimiter.rateLimiter } returns RateLimiter(
            listOf(
                RateLimit(maxRequestsWithinTimeSpan = 50, timeSpanInSeconds = 60)
            )
        )
    }

    @ExperimentalCoroutinesApi
    @Test(expected = FailedToRetrieveProductsException::class)
    fun `WHEN web client THROWS WebRequestUnsuccessfulException THEN throw FailedToRetrieveProductsException`() =
        runBlockingTest {
            coEvery { webApiClient.sendRequest(any()) } throws (FailedToRetrieveProductsException())

            sut.getProductsByName("Peter")
        }
}
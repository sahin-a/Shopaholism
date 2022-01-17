package com.sar.shopaholism.data.web

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.coroutines.awaitString
import com.sar.shopaholism.data.web.exceptions.RateLimitedException
import com.sar.shopaholism.data.web.exceptions.WebRequestUnsuccessfulException
import com.sar.shopaholism.data.web.model.Request
import com.sar.shopaholism.data.web.ratelimiting.RateLimiter
import com.sar.shopaholism.domain.logger.Logger
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class WebApiClientTest {
    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)

    @MockK
    private lateinit var fuelManager: FuelManager

    @MockK(relaxUnitFun = true)
    private lateinit var rateLimiter: RateLimiter

    @RelaxedMockK
    private lateinit var logger: Logger

    @InjectMockKs
    private lateinit var sut: WebApiClientImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = RateLimitedException::class)
    fun `THROWS RateLimitedException WHEN rate limited`() = testScope.runBlockingTest {
        every { rateLimiter.isRateLimited() } returns true

        val request = Request(
            path = "/search",
            method = HttpMethod.GET,
            parameters = mapOf()
        )

        sut.sendRequest(request, testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = WebRequestUnsuccessfulException::class)
    fun `THROWS WebRequestUnsuccessfulException WHEN request fails`() = testScope.runBlockingTest {
        every { rateLimiter.isRateLimited() } returns false

        val request = Request(
            path = "/search",
            method = HttpMethod.GET,
            parameters = mapOf()
        )

        coEvery {
            fuelManager.request(method = any(), path = any(), parameters = any()).awaitString()
        } throws Exception()

        sut.sendRequest(request, testDispatcher)
    }
}
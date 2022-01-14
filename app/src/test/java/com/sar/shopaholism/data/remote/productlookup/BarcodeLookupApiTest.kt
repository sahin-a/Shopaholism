package com.sar.shopaholism.data.remote.productlookup

import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.dao.BarcodeLookupApiRateLimit
import com.sar.shopaholism.data.remote.productlookup.dto.barcodelookupapi.BarcodeProductsDto
import com.sar.shopaholism.data.remote.productlookup.exceptions.FailedToRetrieveProductsException
import com.sar.shopaholism.data.remote.web.WebApiClient
import com.sar.shopaholism.data.remote.web.exceptions.WebRequestUnsuccessfulException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class BarcodeLookupApiTest {

    @MockK(relaxUnitFun = true)
    private lateinit var webApiClient: WebApiClient

    @RelaxedMockK
    private lateinit var rateLimiter: BarcodeLookupApiRateLimit

    @InjectMockKs
    private lateinit var sut: BarcodeLookupApi

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = FailedToRetrieveProductsException::class)
    fun `WHEN web client THROWS WebRequestUnsuccessfulException THEN throw FailedToRetrieveProductsException`() =
        runBlockingTest {
            coEvery {
                webApiClient.sendRequest<BarcodeProductsDto>(any(), any(), any())
            } throws (WebRequestUnsuccessfulException())

            sut.getProductsByName("Peter")
        }
}
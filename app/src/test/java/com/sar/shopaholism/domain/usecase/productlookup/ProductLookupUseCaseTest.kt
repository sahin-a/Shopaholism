package com.sar.shopaholism.domain.usecase.productlookup

import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.entity.productlookup.Store
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.ProductLookupRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class ProductLookupUseCaseTest {

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    @ExperimentalCoroutinesApi
    private val testScope = TestCoroutineScope(testDispatcher)

    @MockK
    private lateinit var repo: ProductLookupRepository

    @MockK
    private lateinit var logger: Logger

    @InjectMockKs
    private lateinit var sut: ProductLookupUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `no exception if items returned`() = testScope.runBlockingTest {
        val name = "Playstation 5"

        coEvery {
            repo.getProductsByName(name)
        } returns listOf(
            Product(
                title = "Playstation 5",
                description = "Peter's Playstation 5",
                images = listOf("peter.png"),
                stores = listOf(
                    Store(
                        price = "200.00",
                        currency = "EUR",
                        name = "NewEgg",
                        url = "www.newegg.com",
                        country = "Uzbekistan"
                    )
                )
            )
        )

        val products = sut.getProductsByName(name, testDispatcher)

        assertTrue(products.count() > 0)
    }

    @ExperimentalCoroutinesApi
    @Test(expected = Exception::class)
    fun `exception thrown if no items returned`() = testScope.runBlockingTest {
        val name = "Playstation 5"

        coEvery {
            sut.getProductsByName(name)
        } returns emptyList()

        sut.getProductsByName(name, testDispatcher)
    }
}
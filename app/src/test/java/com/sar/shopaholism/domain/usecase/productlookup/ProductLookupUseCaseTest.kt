package com.sar.shopaholism.domain.usecase.productlookup

import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.entity.productlookup.Store
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.ProductLookupRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class ProductLookupUseCaseTest {

    private val repo: ProductLookupRepository = mockk()
    private val logger: Logger = mockk()

    private lateinit var useCase: ProductLookupUseCase

    @Before
    fun setup() {
        useCase = ProductLookupUseCase(productLookupRepository = repo, logger = logger)
    }

    @Test
    fun `no exception if items returned`() {
        val name = "Playstation 5"

        every {
            useCase.getProductsByName(name)
        } returns listOf(
            Product(
                title = "Playstation 5",
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

        val products = useCase.getProductsByName(name)

        assertTrue(products.count() > 0)
    }

    @Test(expected = Exception::class)
    fun `exception thrown if no items returned`() {
        val name = "Playstation 5"

        every {
            useCase.getProductsByName(name)
        } returns emptyList()

        val products = useCase.getProductsByName(name)
    }
}
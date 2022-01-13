package com.sar.shopaholism.data.repository

import com.sar.shopaholism.data.remote.productlookup.entity.ProductEntity
import com.sar.shopaholism.data.remote.productlookup.entity.StoreEntity
import com.sar.shopaholism.data.remote.productlookup.source.ProductLookupDataSource
import com.sar.shopaholism.domain.entity.productlookup.Product
import com.sar.shopaholism.domain.entity.productlookup.Store
import com.sar.shopaholism.domain.repository.ProductLookupRepository
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals

class ProductLookupRepositoryImplTest {

    @MockK
    private lateinit var dataSource: ProductLookupDataSource

    @InjectMockKs
    private lateinit var sut: ProductLookupRepository

    @Test
    fun `ProductEntity gets mapped to Product correctly`() = runBlockingTest {

        coEvery {
            dataSource.getProductsByName("Playstation 5")
        } returns listOf(
            ProductEntity(
                title = "Playstation 5",
                description = "Apfelpeter",
                stores = listOf(
                    StoreEntity(
                        name = "Saturn",
                        url = "www.saturn.de",
                        country = "DE",
                        price = "500000000000",
                        currency = "EUR"
                    )
                ),
                images = listOf("hello I am a image link")
            )
        )

        /* Product Mapping */

        val product = Product(
            title = "Playstation 5",
            stores = listOf(
                Store(
                    name = "Saturn",
                    url = "www.saturn.de",
                    country = "DE",
                    price = "500000000000",
                    currency = "EUR"
                )
            ),
            images = listOf("hello I am a image link"),
            description = "Apfelpeter"
        )

        val productEntity = dataSource.getProductsByName("Playstation 5").first()
        assertEquals(expected = product.title, actual = productEntity.title)
        assertEquals(expected = product.description, actual = productEntity.description)
        assertEquals(expected = product.images, actual = productEntity.images)

        /* Store Mapping */

        val storeEntity = productEntity.stores.first()
        val store = product.stores.first()

        assertEquals(expected = store.name, actual = storeEntity.name)
        assertEquals(expected = store.url, actual = storeEntity.url)
        assertEquals(expected = store.price, actual = storeEntity.price)
        assertEquals(expected = store.country, actual = storeEntity.country)
        assertEquals(expected = store.currency, actual = storeEntity.currency)
    }

}
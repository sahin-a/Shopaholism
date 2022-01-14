package com.sar.shopaholism.data.repository

import com.sar.shopaholism.data.local.entity.WishEntity
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.domain.entity.Wish
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WishesRepositoryImplTest {

    @MockK
    private lateinit var dataSource: WishesDataSource

    @InjectMockKs
    private lateinit var sut: WishesRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `update data source call`() = runBlockingTest {
        val id = 2L
        val imageUri = "imageUri"
        val title = "Hallo Test!"
        val description = "Hallo Description!"
        val price = 20.0
        val priority = 5.0

        val wishEntity = WishEntity(
            id = id,
            imageUri = imageUri,
            title = title,
            description = description,
            price = price,
            priority = priority
        )

        val wish = Wish(
            id = id,
            imageUri = imageUri,
            title = title,
            description = description,
            price = price,
            priority = priority
        )

        coEvery { dataSource.updateWish(wishEntity) } returns true

        sut.update(wish)

        coVerify {
            dataSource.updateWish(wishEntity)
        }
    }

    @Test
    fun `delete data source call`() = runBlockingTest {
        val id = 5L

        coEvery { dataSource.deleteWish(id) } returns true

        sut.delete(id)

        coVerify { dataSource.deleteWish(id) }
    }

    @Test
    fun `getWishes data source call`() = runBlockingTest {
        coEvery { dataSource.getWishes() } returns flowOf(listOf())

        sut.getWishes()

        coVerify { dataSource.getWishes() }
    }

    @Test
    fun `get wish data source call`() = runBlockingTest {
        val id = 2L

        coEvery { dataSource.getWish(id) } returns WishEntity(
            id = id,
            title = "",
            imageUri = "",
            description = "",
            price = 1.0,
            priority = 1.0)

        sut.getWish(id)

        coVerify { dataSource.getWish(id) }
    }

    @Test
    fun `create wish data source call`() = runBlockingTest {
        val id = 0L
        val imageUri = "New Uri"
        val title = "New Wish"
        val description = "Epic Description"
        val price = 20.0
        val priority = 5.0

        val wishEntity = WishEntity(
            id = id,
            imageUri = imageUri,
            title = title,
            description = description,
            price = price,
            priority = priority
        )

        val wish = Wish(
            id = id,
            imageUri = imageUri,
            title = title,
            description = description,
            price = price,
            priority = priority
        )

        coEvery { dataSource.insert(wishEntity) } returns 1

        sut.create(wish)

        coVerify {
            dataSource.insert(wishEntity)
        }
    }

}
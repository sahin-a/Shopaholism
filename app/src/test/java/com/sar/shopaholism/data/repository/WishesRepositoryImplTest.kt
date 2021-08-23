package com.sar.shopaholism.data.repository

import com.sar.shopaholism.data.local.entity.WishEntity
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.domain.entity.Wish
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

class WishesRepositoryImplTest {
    @MockK
    private var dataSource: WishesDataSource = mockk(relaxed = true)

    private val wishesRepositoryImpl = WishesRepositoryImpl(dataSource)

    @BeforeEach
    fun setUpMocks() {
        clearAllMocks()
    }

    @Test
    fun `update data source call`() = runBlockingTest {
        val id = 2L
        val title = "Hallo Test!"
        val description = "Hallo Description!"
        val price = 20.0
        val priority = 5

        wishesRepositoryImpl.update(
            Wish(
                id,
                title,
                description,
                price,
                priority
            )
        )

        coVerify {
            dataSource.updateWish(
                WishEntity(
                    id,
                    title,
                    description,
                    price,
                    priority
                )
            )
        }
    }

    @Test
    fun `delete data source call`() = runBlockingTest {
        val id = 5L

        wishesRepositoryImpl.delete(id)

        coVerify { dataSource.deleteWish(id) }
    }

    @Test
    fun `getWishes data source call`() = runBlockingTest {
        wishesRepositoryImpl.getWishes()

        coVerify { dataSource.getWishes() }
    }

    @Test
    fun `get wish data source call`() = runBlockingTest {
        val id = 2L

        wishesRepositoryImpl.getWish(id)

        coVerify { dataSource.getWish(id) }
    }

    @Test
    fun `create wish data source call`() = runBlockingTest {
        val id = 0L
        val title = "New Wish"
        val description = "Epic Description"
        val price = 20.0
        val priority = 5

        val wish = Wish(
            id,
            title,
            description,
            price,
            priority
        )

        wishesRepositoryImpl.create(wish)

        coVerify {
            dataSource.insert(
                WishEntity(
                    id,
                    title,
                    description,
                    price,
                    priority
                )
            )
        }
    }

}
package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CreateWishUseCaseTest {

    private lateinit var logger: Logger
    private lateinit var wishesRepository: WishesRepository
    private lateinit var createWishUseCase: CreateWishUseCase

    @Before
    fun setup() {
        logger = mockk(relaxed = true)
        wishesRepository = mockk()
        createWishUseCase = CreateWishUseCase(wishesRepository, logger)
    }

    @Test
    fun `create fun from repository gets called`() = runBlockingTest {
        val wish = Wish(
            id = 0L,
            title = "New Wish",
            imageUri = "New Image Uri",
            description = "Epic Description",
            price = 20.0,
            priority = 0
        )

        coEvery { wishesRepository.create(wish) } returns 1L

        createWishUseCase.execute(
            title = wish.title,
            imageUri = wish.imageUri,
            description = wish.description,
            price = wish.price
        )

        coVerify { wishesRepository.create(wish) }
    }

    @Test(expected = WishNotCreatedException::class)
    fun `Creation fails returns WishNotCreatedException`() = runBlockingTest {
        val wish = Wish(
            id = 0L,
            title = "New Wish",
            imageUri = "New Image Uri",
            description = "Epic Description",
            price = 20.0,
            priority = 0
        )

        coEvery {
            wishesRepository.create(any())
        } returns 0

        createWishUseCase.execute(
            title = wish.title,
            imageUri = wish.imageUri,
            description = wish.description,
            price = wish.price
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Blank title parameter throws IllegalArgumentException`() = runBlockingTest {
        createWishUseCase.execute(
            title = "",
            imageUri = "image lol",
            description = "description",
            price = 1.0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Blank description parameter throws IllegalArgumentException`() = runBlockingTest {
        createWishUseCase.execute(
            title = "title",
            imageUri = "ahh",
            description = "",
            price = 1.0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Negative price parameter throws IllegalArgumentException`() = runBlockingTest {
        createWishUseCase.execute(
            title = "title",
            imageUri = "meddl",
            description = "description",
            price = -1.0
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Blank imageUri parameter throws IllegalArgumentException`() = runBlockingTest {
        createWishUseCase.execute(
            title = "title",
            imageUri = "",
            description = "description",
            price = 1.0
        )
    }

}
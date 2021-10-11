package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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
    fun `create func from repository gets called`() = runBlockingTest {
        val id = 0L
        val title = "Title"
        val imageUri = "ImageUri"
        val description = "Description"
        val price = 20.0
        val priority = 0.0

        /*
        * Probably fails because the hash of the objects don't match
        * */

        coEvery {
            wishesRepository.create(any())
        } returns 1

        createWishUseCase.execute(
            title = title,
            imageUri = imageUri,
            description = description,
            price = price
        )

        coVerify {
            wishesRepository.create(
                coWithArg { wish ->
                    assertEquals(wish.id, id)
                    assertEquals(wish.title, title)
                    assertEquals(wish.description, description)
                    assertEquals(wish.imageUri, imageUri)
                    assertEquals(wish.price, price)
                    assertEquals(wish.priority, priority)
                }
            )
        }
    }

    @Test(expected = WishNotCreatedException::class)
    fun `Creation fails returns WishNotCreatedException`() = runBlockingTest {
        val title = "New Wish"
        val imageUri = "New Image Uri"
        val description = "Epic Description"
        val price = 20.0

        coEvery {
            wishesRepository.create(any())
        } returns 0L

        createWishUseCase.execute(
            title = title,
            imageUri = imageUri,
            description = description,
            price = price
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
    fun `Negative price parameter throws IllegalArgumentException`() = runBlockingTest {
        createWishUseCase.execute(
            title = "title",
            imageUri = "meddl",
            description = "description",
            price = -1.0
        )
    }

}
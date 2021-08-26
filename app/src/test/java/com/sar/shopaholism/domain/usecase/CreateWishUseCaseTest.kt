package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateWishUseCaseTest {
    private var logger: Logger = mockk()
    private var wishesRepository: WishesRepository = mockk()
    private lateinit var createWishUseCase: CreateWishUseCase

    @BeforeEach
    fun setup() {
        every { logger.e(any(), any(), any()) } returns Unit
        every { logger.i(any(), any()) } returns Unit

        createWishUseCase = CreateWishUseCase(wishesRepository, logger)
    }

    @Test
    fun `create fun from repository gets called`() = runBlockingTest {
        val wish = Wish(
            id = 0,
            title = "New Wish",
            imageUri = "New Image Uri",
            description = "Epic Description",
            price = 20.0,
            priority = 0
        )

        coEvery {
            wishesRepository.create(wish)
        } returns 1

        createWishUseCase.execute(
            title = "New Wish",
            imageUri = "New Image Uri",
            description = "Epic Description",
            price = 20.0
        )

        coVerify { wishesRepository.create(wish) }
    }

    @Test
    fun `Creation fails returns WishNotCreatedException`() = runBlockingTest {
        coEvery {
            wishesRepository.create(any())
        } returns 0

        assertThrows<WishNotCreatedException> {
            createWishUseCase.execute(
                title = "New Wish",
                imageUri = "New Image Uri",
                description = "Epic Description",
                price = 20.0
            )
        }
    }

    @Test
    fun `Blank title parameter throws IllegalArgumentException`() = runBlockingTest {
        assertThrows<IllegalArgumentException> {
            createWishUseCase.execute(
                title = "",
                imageUri = "image lol",
                description = "description",
                price = 1.0
            )
        }
    }

    @Test
    fun `Blank description parameter throws IllegalArgumentException`() = runBlockingTest {
        assertThrows<IllegalArgumentException> {
            createWishUseCase.execute(
                title = "title",
                imageUri = "ahh",
                description = "",
                price = 1.0
            )
        }
    }

    @Test
    fun `Negative price parameter throws IllegalArgumentException`() = runBlockingTest {
        assertThrows<IllegalArgumentException> {
            createWishUseCase.execute(
                title = "title",
                imageUri = "meddl",
                description = "description",
                price = -1.0
            )
        }
    }

    @Test
    fun `Blank imageUri parameter throws IllegalArgumentException`() = runBlockingTest {
        assertThrows<IllegalArgumentException> {
            createWishUseCase.execute(
                title = "title",
                imageUri = "",
                description = "description",
                price = 1.0
            )
        }
    }

}
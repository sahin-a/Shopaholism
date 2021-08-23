package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateWishUseCaseTest {
    private var wishesRepository: WishesRepository = mockk()
    private lateinit var createWishUseCase: CreateWishUseCase

    @BeforeEach
    fun setup() {
        createWishUseCase = CreateWishUseCase(wishesRepository)
    }

    @Test
    fun `Creation fails returns WishNotCreatedException`() = runBlockingTest {
        coEvery {
            wishesRepository.create(any())
        } returns 0

        assertThrows<WishNotCreatedException> {
            createWishUseCase.execute(
                title = "New Wish",
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
                description = "description",
                price = -1.0
            )
        }
    }

}
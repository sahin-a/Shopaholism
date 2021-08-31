package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteWishUseCaseTest {
    private val wishesRepository: WishesRepository = mockk()
    private lateinit var deleteWishUseCase: DeleteWishUseCase

    @Before
    fun setup() {
        deleteWishUseCase = DeleteWishUseCase(wishesRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Non positive Id, throws IllegalArgumentException`() = runBlockingTest {
        deleteWishUseCase.execute(0)
    }

    @Test(expected = WishNotDeletedException::class)
    fun `Wish not found, throws WishNotDeletedException`() = runBlockingTest {
        coEvery { wishesRepository.delete(any()) } returns false

        deleteWishUseCase.execute(1L)
    }
}
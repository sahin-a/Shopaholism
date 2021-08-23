package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteWishUseCaseTest {
    private val wishesRepository: WishesRepository = mockk()
    private lateinit var deleteWishUseCase: DeleteWishUseCase

    @BeforeEach
    fun setup() {
        deleteWishUseCase = DeleteWishUseCase(wishesRepository)
    }

    @Test
    fun `Non positive Id, throws IllegalArgumentException`() = runBlockingTest {
        assertThrows<IllegalArgumentException> { deleteWishUseCase.execute(0) }
    }

    @Test
    fun `Wish not found, throws WishNotDeletedException`() = runBlockingTest {
        coEvery { wishesRepository.delete(any()) } returns false

        assertThrows<WishNotDeletedException> {
            deleteWishUseCase.execute(1L)
        }
    }
}
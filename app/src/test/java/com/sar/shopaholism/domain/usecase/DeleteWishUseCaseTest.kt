package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteWishUseCaseTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @MockK
    private lateinit var wishesRepository: WishesRepository

    @InjectMockKs
    private lateinit var deleteWishUseCase: DeleteWishUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test(expected = WishNotDeletedException::class)
    fun `Wish not found, throws WishNotDeletedException`() = testScope.runBlockingTest {
        coEvery { wishesRepository.delete(any()) } returns false

        deleteWishUseCase.execute(1L, testDispatcher)
    }
}
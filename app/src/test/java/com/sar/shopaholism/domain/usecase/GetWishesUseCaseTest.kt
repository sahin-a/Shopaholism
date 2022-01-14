package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetWishesUseCaseTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @MockK
    private lateinit var wishesRepository: WishesRepository

    @InjectMockKs
    private lateinit var getWishesUseCase: GetWishesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Get Wishes from repo`() = testScope.runBlockingTest {
        coEvery { wishesRepository.getWishes() } returns flowOf(emptyList())

        getWishesUseCase.execute(testDispatcher)

        coVerify { wishesRepository.getWishes() }
    }
}
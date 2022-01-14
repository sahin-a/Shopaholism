package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotFoundException
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
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class GetWishUseCaseTest : KoinComponent {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @MockK
    private lateinit var wishesRepository: WishesRepository

    @InjectMockKs
    private lateinit var getWishUseCase: GetWishUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test(expected = WishNotFoundException::class)
    fun `Id doesn't exist, rethrows WishNotFoundException`() = testScope.runBlockingTest {
        coEvery { wishesRepository.getWish(any()) } throws NullPointerException()
        getWishUseCase.execute(wishId = 1L, testDispatcher)
    }
}
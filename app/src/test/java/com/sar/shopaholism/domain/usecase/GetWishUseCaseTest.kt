package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.component.KoinComponent

@ExperimentalCoroutinesApi
class GetWishUseCaseTest : KoinComponent {
    private val wishesRepository: WishesRepository = mockk()
    private lateinit var getWishUseCase: GetWishUseCase

    @Before
    fun setup() {
        getWishUseCase = GetWishUseCase(wishesRepository)
    }

    @Test(expected = WishNotFoundException::class)
    fun `Id doesn't exist, rethrows WishNotFoundException`() = runBlockingTest {
        coEvery { wishesRepository.getWish(any()) } throws NullPointerException()
        getWishUseCase.execute(wishId = 1L)
    }
}
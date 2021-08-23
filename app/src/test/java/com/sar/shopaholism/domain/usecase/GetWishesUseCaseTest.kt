package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetWishesUseCaseTest {
    private val wishesRepository: WishesRepository = mockk()
    private lateinit var getWishesUseCase: GetWishesUseCase

    @BeforeEach
    fun setup() {
        getWishesUseCase = GetWishesUseCase(wishesRepository)
    }

    @Test
    fun `Get Wishes from repo`() = runBlockingTest {
        coEvery { wishesRepository.getWishes() } returns flowOf(emptyList())

        getWishesUseCase.execute()

        coVerify { wishesRepository.getWishes() }
    }
}
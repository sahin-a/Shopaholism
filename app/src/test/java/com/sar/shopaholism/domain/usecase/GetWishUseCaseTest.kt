package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.repository.WishesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.component.KoinComponent

class GetWishUseCaseTest : KoinComponent {
    private val wishesRepository: WishesRepository = mockk()
    private lateinit var getWishUseCase: GetWishUseCase

    @BeforeEach
    fun setup() {
        getWishUseCase = GetWishUseCase(wishesRepository)
    }

    @Test
    fun `Id doesn't exist, rethrows WishNotFoundException`() = runBlockingTest {
        coEvery { wishesRepository.getWish(any()) } throws NullPointerException()
        assertThrows<WishNotFoundException> { getWishUseCase.execute(wishId = 1L) }
    }
}
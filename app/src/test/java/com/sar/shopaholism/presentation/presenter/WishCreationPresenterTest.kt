package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.rater.WishesRater
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest

class WishCreationPresenterTest : KoinTest {
    @RelaxedMockK
    private val logger: Logger = mockk(relaxed = true)

    @RelaxedMockK
    private lateinit var wishesRater: WishesRater

    @MockK
    private lateinit var createWishUseCase: CreateWishUseCase

    @MockK
    private lateinit var view: WishCreationView

    @MockK
    private lateinit var wishFeedbackService: WishFeedbackService

    @InjectMockKs
    private lateinit var presenter: WishCreationPresenter


    @Before
    fun setup() {
        presenter.attachView(view)
    }

    @Test
    fun `Create wish calls CreateWishUseCase`() = runBlockingTest {
        val title = "Epic title"
        val imageUri = "epicFile.png"
        val description = "Epic description"
        val price = 20.0

        coEvery {
            createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )
        } returns 1

        presenter.createWish(
            title,
            imageUri,
            description,
            price
        )

        coVerify {
            createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )
        }
    }
}
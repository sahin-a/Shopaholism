package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.rater.WishesRater
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest

class WishCreationPresenterTest : KoinTest {
    private val logger: Logger = mockk(relaxed = true)
    private val createWishUseCase: CreateWishUseCase = mockk()
    private val wishesRater: WishesRater = mockk(relaxed = true)
    private val view: WishCreationView = mockk()

    private lateinit var presenter: WishCreationPresenter


    @Before
    fun setup() {
        presenter = WishCreationPresenter(
            createWishUseCase = createWishUseCase,
            wishesRater = wishesRater,
            logger = logger
        )
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
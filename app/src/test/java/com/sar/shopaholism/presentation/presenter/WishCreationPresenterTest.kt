package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.di.presentationModules
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class WishCreationPresenterTest : KoinTest {
    private val logger: Logger = mockk(relaxed = true)
    private val createWishUseCase: CreateWishUseCase = mockk()
    private val view: WishCreationView = mockk()

    private lateinit var presenter: WishCreationPresenter

    @Before
    fun setup() {
        presenter = WishCreationPresenter(createWishUseCase)
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
        } just Runs

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
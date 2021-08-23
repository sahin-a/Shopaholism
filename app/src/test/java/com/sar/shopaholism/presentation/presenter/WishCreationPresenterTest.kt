package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.di.presentationModules
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WishCreationPresenterTest : KoinTest {
    private val createWishUseCase: CreateWishUseCase = mockk(relaxed = true)
    private val logger: Logger = mockk(relaxed = true)
    val view: WishCreationView = mockk(relaxed = true)

    private lateinit var presenter: WishCreationPresenter

    // TODO: FIX DI SHIT NOT WORKING
    @BeforeAll
    fun doDi() {
        startKoin {
            modules(
                module {
                    presentationModules
                }
            )
        }
    }

    @BeforeEach
    fun setup() {
        presenter = WishCreationPresenter(createWishUseCase)
        presenter.attachView(view)
    }

    @Test
    fun `Create wish calls CreateWishUseCase`() = runBlockingTest {
        every { presenter.view!!.navigateTo(any()) } returns Unit

        val title = "Epic title"
        val description = "Epic description"
        val price = 20.0

        presenter.createWish(
            title,
            description,
            price
        )

        coVerify { createWishUseCase.execute(title, description, price) }
    }

    @Test
    fun `After successfully creating wish navigate back to overview`() {

    }
}
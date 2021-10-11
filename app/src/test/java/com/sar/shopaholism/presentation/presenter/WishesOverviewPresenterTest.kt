package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class WishesOverviewPresenterTest {

    private lateinit var presenter: WishesOverviewPresenter
    private lateinit var view: WishesOverviewView
    private lateinit var getWishesUseCase: GetWishesUseCase
    private lateinit var model: WishesModel
    private lateinit var logger: Logger

    @Before
    fun setup() {
        getWishesUseCase = mockk()
        logger = mockk(relaxed = true)
        view = mockk()
        model = WishesModel()

        presenter = WishesOverviewPresenter(
            getWishesUseCase = getWishesUseCase,
            model = model,
            logger = logger
        )
    }

    @Test
    fun `loadWishes gets wishes from GetWishesUseCase and stores it in model`() = runBlocking(
        Dispatchers.IO) {

        val wish = Wish(
            id = 343L,
            title = "title",
            imageUri = "imageUri",
            description = "description",
            price = 53.0,
            priority = 2.0
        )

        coEvery { getWishesUseCase.execute() } returns flowOf(listOf(wish))

        presenter.loadWishes()

        assertTrue { model.wishes.value!!.size == 1 }
    }

    @Test
    fun `navigateToCreateWishFragment navigates to createWishFragment`() {
        presenter.attachView(view)

        every {
            presenter.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)
        } just Runs

        presenter.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)

        verify { presenter.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment) }
    }

}
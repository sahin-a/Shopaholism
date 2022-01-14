package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class WishesOverviewPresenterTest : BasePresenterTest() {
    @SpyK
    private var model: WishesModel = WishesModel()

    @MockK(relaxUnitFun = true)
    private lateinit var view: WishesOverviewView

    @MockK
    private lateinit var getWishesUseCase: GetWishesUseCase

    @RelaxedMockK
    private lateinit var logger: Logger

    @InjectMockKs
    private lateinit var sut: WishesOverviewPresenter

    @ExperimentalCoroutinesApi
    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `loadWishes gets wishes from GetWishesUseCase and stores it in model`() = runBlockingTest {
        val wish = Wish(
            id = 343L,
            title = "title",
            imageUri = "imageUri",
            description = "description",
            price = 53.0,
            priority = 2.0
        )

        coEvery { getWishesUseCase.execute() } returns flowOf(listOf(wish))

        sut.loadWishes()

        assertTrue { model.wishes.value!!.size == 1 }
    }

    @Test
    fun `navigateToCreateWishFragment navigates to createWishFragment`() {
        sut.attachView(view)

        every {
            sut.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)
        } just Runs

        sut.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)

        verify { sut.view!!.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment) }
    }

}
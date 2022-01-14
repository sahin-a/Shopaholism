package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.rater.WishesRater
import com.sar.shopaholism.presentation.view.WishDeletionView
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class WishDeletionPresenterTest : BasePresenterTest() {

    @RelaxedMockK
    private lateinit var wishesRater: WishesRater

    @MockK(relaxUnitFun = true)
    private lateinit var wishFeedbackService: WishFeedbackService

    @MockK
    private lateinit var view: WishDeletionView

    @MockK
    private lateinit var deleteWishUseCase: DeleteWishUseCase

    @MockK
    private lateinit var getWishUseCase: GetWishUseCase

    @InjectMockKs
    private lateinit var presenter: WishDeletionPresenter

    @ExperimentalCoroutinesApi
    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
    }

    @Test
    fun `getWish calls getWishUseCase`() = runBlockingTest {
        val id = 223L

        coEvery { getWishUseCase.execute(id) } returns Wish(
            id = id,
            title = "title",
            imageUri = "imageUri",
            description = "description",
            price = 2.0,
            priority = 0.0
        )

        presenter.getWish(id)

        coVerify { getWishUseCase.execute(id) }
    }

    @Test
    fun `deleteWish calls deleteWishUseCase`() = runBlockingTest {
        val id = 423L

        coEvery { deleteWishUseCase.execute(id) } just Runs

        presenter.deleteWish(id)

        coVerify { deleteWishUseCase.execute(id) }
    }
}
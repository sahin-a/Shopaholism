package com.sar.shopaholism.presentation.presenter

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

@ExperimentalCoroutinesApi
class WishDeletionPresenterTest : BasePresenterTest() {

    @RelaxedMockK
    private lateinit var wishesRater: WishesRater

    @MockK(relaxUnitFun = true)
    private lateinit var wishFeedbackService: WishFeedbackService

    @MockK(relaxUnitFun = true)
    private lateinit var view: WishDeletionView

    @MockK
    private lateinit var deleteWishUseCase: DeleteWishUseCase

    @MockK
    private lateinit var getWishUseCase: GetWishUseCase

    @InjectMockKs
    private lateinit var sut: WishDeletionPresenter

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
    }

    @Test
    fun `deleteWish calls deleteWishUseCase`() = runBlockingTest {
        val id = 423L

        every { view.getWishId() } returns id
        coEvery { deleteWishUseCase.execute(id) } just Runs

        sut.deleteWish()

        coVerify { deleteWishUseCase.execute(id) }
    }

    @Test
    fun `WHEN exception is thrown during deletion THEN call onFailure`() = runBlockingTest {
        val id = 423L

        every { view.getWishId() } returns id
        coEvery { deleteWishUseCase.execute(id) } throws (Exception())

        sut.deleteWish()

        verify { view.onFailure() }
    }
}
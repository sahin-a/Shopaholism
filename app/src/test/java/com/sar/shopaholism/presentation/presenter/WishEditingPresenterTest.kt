package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishEditingView
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WishEditingPresenterTest : BasePresenterTest() {
    @MockK(relaxUnitFun = true)
    private lateinit var view: WishEditingView

    @MockK
    private lateinit var getWishUseCase: GetWishUseCase

    @MockK
    private lateinit var updateWishUseCase: UpdateWishUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var wishFeedbackService: WishFeedbackService

    @SpyK
    private var createWishModel = CreateWishModel()

    @InjectMockKs
    private lateinit var presenter: WishEditingPresenter

    @Before
    override fun setup() {
        super.setup()
        MockKAnnotations.init(this)
    }

    @Test
    fun `onAttachView calls getWishUseCase to retrieve original data`() {
        val id = 221L

        coEvery { getWishUseCase.execute(id) } returns Wish(
            id = id,
            title = "title",
            imageUri = "imageUri",
            description = "description",
            price = 2.0,
            priority = 0.0
        )

        every { view.getWishId() } returns id

        presenter.attachView(view)

        coVerify { getWishUseCase.execute(id) }
    }

    @Test
    fun `updateWish calls UpdateWishUseCase`() = runBlockingTest {
        val wish = Wish(
            id = 0,
            title = "title",
            imageUri = "imageUri",
            description = "description",
            price = 2.0,
            priority = 0.0
        )

        coEvery { updateWishUseCase.execute(any()) } just Runs

        wish.apply {
            presenter.updateWish(
                id = id,
                title = title,
                description = description,
                imageUri = imageUri,
                price = price
            )
        }

        coVerify {
            updateWishUseCase.execute(
                coWithArg { pWish ->
                    wish.apply {
                        assertEquals(pWish.title, title)
                        assertEquals(pWish.id, id)
                        assertEquals(pWish.title, title)
                        assertEquals(pWish.description, description)
                        assertEquals(pWish.imageUri, imageUri)
                        assertEquals(pWish.price, price)
                        assertEquals(pWish.priority, priority)
                    }
                }
            )
        }
    }

}
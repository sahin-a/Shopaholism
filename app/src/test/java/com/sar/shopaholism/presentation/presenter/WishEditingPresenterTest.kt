package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.view.WishEditingView
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

@ExperimentalCoroutinesApi
class WishEditingPresenterTest {

    private lateinit var presenter: WishEditingPresenter
    private lateinit var view: WishEditingView
    private lateinit var getWishUseCase: GetWishUseCase
    private lateinit var updateWishUseCase: UpdateWishUseCase

    @Before
    fun setup() {
        getWishUseCase = mockk()
        updateWishUseCase = mockk()
        view = mockk()
        presenter = WishEditingPresenter(
            updateWishUseCase = updateWishUseCase,
            getWishUseCase = getWishUseCase
        )
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
            priority = 0
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
            priority = 0
        )

        coEvery { updateWishUseCase.execute(wish) } just Runs

        wish.apply {
            presenter.updateWish(
                id = id,
                title = title,
                description = description,
                imageUri = imageUri,
                price = price
            )
        }

        coVerify { updateWishUseCase.execute(wish) }
    }

}
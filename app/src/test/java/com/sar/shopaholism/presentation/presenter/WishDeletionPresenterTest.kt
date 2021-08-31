package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.view.WishDeletionView
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class WishDeletionPresenterTest {

    private lateinit var presenter: WishDeletionPresenter
    private lateinit var view: WishDeletionView
    private lateinit var deleteWishUseCase: DeleteWishUseCase
    private lateinit var getWishUseCase: GetWishUseCase

    @Before
    fun setup() {
        deleteWishUseCase = mockk()
        getWishUseCase = mockk()
        presenter = WishDeletionPresenter(
            deleteWishUseCase = deleteWishUseCase,
            getWishUseCase = getWishUseCase
        )
        view = mockk()
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
            priority = 0
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
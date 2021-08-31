package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.*
import org.junit.Before
import org.junit.Test

class BaseWishCreationPresenterTest {

    private lateinit var view: WishCreationView
    private lateinit var presenter: BaseWishCreationPresenter<WishCreationView>

    @Before
    fun setup() {
        view = mockk()
        presenter = BaseWishCreationPresenter<WishCreationView>()

        presenter.attachView(view)
    }

    @Test
    fun `getData sets data in view`() {

        val wish = Wish(
            id = 0,
            title = "Abdool",
            imageUri = "imageUri",
            description = "description",
            price = 1.0,
            priority = 0
        )

        presenter.model.apply {
            this.title = wish.title
            this.imageUri = wish.imageUri
            this.description = wish.description
            this.price = wish.price
        }

        every {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = wish.price.toString()
            )
        } just Runs

        presenter.getData()

        verify {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = wish.price.toString()
            )
        }
    }

    @Test
    fun `getData sets empty string if price lower than 0`() {
        val wish = Wish(
            id = 0,
            title = "Abdool",
            imageUri = "imageUri",
            description = "description",
            price = -0.1,
            priority = 0
        )

        presenter.model.apply {
            this.title = wish.title
            this.imageUri = wish.imageUri
            this.description = wish.description
            this.price = wish.price
        }

        every {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = ""
            )
        } just Runs

        presenter.getData()

        verify {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = ""
            )
        }
    }
}
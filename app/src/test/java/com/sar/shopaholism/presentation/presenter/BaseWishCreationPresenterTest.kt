package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishCreationView
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.Before
import org.junit.Test

class BaseWishCreationPresenterTest : BasePresenterTest() {
    @MockK
    private lateinit var view: WishCreationView

    @SpyK
    private var createWishModel = CreateWishModel()

    @InjectMockKs
    private lateinit var sut: BaseWishCreationPresenter<WishCreationView>

    @Before
    override fun setup() {
        super.setup()

        MockKAnnotations.init(this)
        sut.attachView(view)
    }

    @Test
    fun `getData sets data in view`() {

        val wish = Wish(
            id = 0,
            title = "Abdool",
            imageUri = "imageUri",
            description = "description",
            price = 1.0,
            priority = 0.0
        )

        sut.model.apply {
            title = wish.title
            imageUri = wish.imageUri
            description = wish.description
            price = wish.price
        }

        every {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = wish.price.toString()
            )
        } just Runs

        sut.getData()

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
            priority = 0.0
        )

        sut.model.apply {
            title = wish.title
            imageUri = wish.imageUri
            description = wish.description
            price = wish.price
        }

        every {
            view.setData(
                title = wish.title,
                imageUri = wish.imageUri,
                description = wish.description,
                price = ""
            )
        } just Runs

        sut.getData()

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
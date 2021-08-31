package com.sar.shopaholism.presentation.fragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class WishesOverviewFragmentTest {

    private val fragment = WishesOverviewFragment()

    // Di
    private val getWishesUseCase: GetWishesUseCase = mockk()
    private val model: WishesModel = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val presenter = WishesOverviewPresenter(
        getWishesUseCase = getWishesUseCase,
        model = model,
        logger = logger
    )

    private val testModule: Module = module() {
        single<WishesOverviewPresenter> { presenter }
    }

    private val wishes = listOf(
        Wish(
            id = 2,
            title = "title",
            description = "description",
            imageUri = "imageUri",
            price = 2.0,
            priority = 0
        )
    )

    @Before
    fun setup() {
        loadKoinModules(testModule)

        every {
            model.wishes.value
        } returns wishes.toMutableList()
    }

    @Test
    fun loads_wishes_after_view_created() {

    }
}
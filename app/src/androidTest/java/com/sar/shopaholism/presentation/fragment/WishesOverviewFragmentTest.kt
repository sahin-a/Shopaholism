package com.sar.shopaholism.presentation.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sar.shopaholism.R
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class WishesOverviewFragmentTest {

    private lateinit var presenter: WishesOverviewPresenter

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

    }

    @Test
    fun loads_wishes_after_view_created() {
        val scenario = launchFragmentInContainer<WishesOverviewFragment>()

        onView(withId(R.id.wishes_overview)).check(matches(isDisplayed()))
    }
}
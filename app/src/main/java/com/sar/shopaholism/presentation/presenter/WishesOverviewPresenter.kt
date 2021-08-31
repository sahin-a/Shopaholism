package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.R
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent

class WishesOverviewPresenter(
    private val getWishesUseCase: GetWishesUseCase,
    private val model: WishesModel,
    private val logger: Logger
) :
    BasePresenter<WishesOverviewView>(),
    KoinComponent {

    companion object {
        private const val TAG: String = "WishesOverviewPresenter"
    }

    fun loadWishes() = runBlocking {
        view?.showLoading(visible = true)

        // retrieving the wishes from the local db
        getWishesUseCase.execute()
            .catch { e -> logger.e(TAG, e.message ?: "") }
            .collect { wishesFromDb ->

                model.wishes.value?.let {
                    it.clear()
                    it.addAll(wishesFromDb)

                    logger.i(
                        TAG,
                        message = "Retrieved ${wishesFromDb.size} Wishes from the local Db"
                    )

                    view?.showWishes(it)
                    view?.showLoading(visible = false)
                }
            }
    }

    fun navigateToCreateWishFragment() {
        view?.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)
        logger.i(tag = this::class.java.name, message = "Navigated to createWishFragment")
    }
}
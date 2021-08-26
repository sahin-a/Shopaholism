package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.R
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun loadWishes() {
        view?.let { view ->
            GlobalScope.launch {
                view.showLoading(visible = true)
                view.enableButtons(enabled = false)

                // retrieving the wishes from the local db
                getWishesUseCase.execute()
                    .catch { e -> logger.e(TAG, e.message ?: "") }
                    .collect { wishesFromDb ->
                        withContext(Dispatchers.Main) {
                            model.wishes.value?.let {
                                it.clear()
                                it.addAll(wishesFromDb)

                                logger.i(
                                    TAG,
                                    message = "Retrieved ${wishesFromDb.size} Wishes from the local Db"
                                )

                                view.showWishes(it)

                                view.showLoading(visible = false)
                                view.enableButtons(enabled = true)
                            }
                        }
                    }
            }
        }
    }

    fun createWish() {
        view?.navigateTo(R.id.action_wishesOverviewFragment_to_createWishFragment)
        logger.i(tag = this::class.java.name, message = "Navigated to createWishFragment")
    }
}
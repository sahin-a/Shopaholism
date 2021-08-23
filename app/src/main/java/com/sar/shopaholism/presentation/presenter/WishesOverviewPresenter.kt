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

    fun loadWishes() {
        GlobalScope.launch {
            // retrieving the wishes from the local db
            getWishesUseCase.execute()
                .catch { e -> logger.e(tag = this::class.java.name, e.localizedMessage) }
                .collect { wishesFromDb ->
                    withContext(Dispatchers.Main) {
                        model.wishes.value?.let {
                            it.clear()
                            it.addAll(wishesFromDb)

                            logger.i(
                                tag = this::class.java.name,
                                message = "Retrieved ${wishesFromDb.size} Wishes from the local Db"
                            )

                            view?.showWishes(it)
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
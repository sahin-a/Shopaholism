package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.view.WishesOverviewView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class WishesOverviewPresenter(
    private val getWishesUseCase: GetWishesUseCase,
    private val createWishUseCase: CreateWishUseCase,
) :
    BasePresenter<WishesOverviewView>(),
    KoinComponent {

    private var model: WishesModel = WishesModel()

    init {
    }

    fun createWish() {

    }

    fun loadWishes() {
        GlobalScope.launch {
            // retrieving the wishes from the local db
            getWishesUseCase.execute()
                .catch { }
                .collect { model.wishes.value!!.addAll(it) }
        }

        view!!.showWishes(model.wishes.value!!)
    }
}
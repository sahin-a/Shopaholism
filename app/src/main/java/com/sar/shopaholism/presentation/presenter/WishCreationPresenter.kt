package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.view.WishCreationView

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase
) : BasePresenter<WishCreationView>() {

    suspend fun createWish(
        title: String,
        description: String,
        price: Double
    ) {
        createWishUseCase.execute(title, description, price)
    }
}
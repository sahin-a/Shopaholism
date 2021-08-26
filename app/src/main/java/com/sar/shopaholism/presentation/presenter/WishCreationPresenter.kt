package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.view.WishCreationView

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase
) : BaseWishCreationPresenter<WishCreationView>() {
    suspend fun createWish(
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ) {
        createWishUseCase.execute(
            title = title,
            imageUri = imageUri,
            description = description,
            price = price
        )
    }
}
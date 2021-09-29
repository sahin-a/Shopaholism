package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.view.WishCreationView

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase,
    private val getWishesUseCase: GetWishesUseCase,
    private val updateWishUseCase: UpdateWishUseCase,
    private val logger: Logger
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
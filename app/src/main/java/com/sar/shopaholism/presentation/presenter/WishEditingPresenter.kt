package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.view.WishEditingView

class WishEditingPresenter(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishUseCase: GetWishUseCase
) : BasePresenter<WishEditingView>() {

    suspend fun getOriginalWish(wishId: Long): Wish {
        // TODO: Exception handling
        return getWishUseCase.execute(wishId)
    }

    suspend fun updateWish(id: Long, title: String, description: String, price: Double) {
        updateWishUseCase.execute(Wish(id, title, description, price, priority = 0))
    }
}
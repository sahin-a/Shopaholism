package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.view.WishDeletionView

class WishDeletionPresenter(
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getWishUseCase: GetWishUseCase
) : BasePresenter<WishDeletionView>() {

    suspend fun getWish(wishId: Long): Wish {
        return getWishUseCase.execute(wishId)
    }

    suspend fun deleteWish(wishId: Long): Boolean {
        try {
            deleteWishUseCase.execute(wishId)
            return true
        } catch (e: WishNotDeletedException) {

        } catch (e: IllegalArgumentException) {

        }

        return false
    }
}
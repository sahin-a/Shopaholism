package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.rater.WishesRater
import com.sar.shopaholism.presentation.view.WishDeletionView

class WishDeletionPresenter(
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishesRater: WishesRater
) : BasePresenter<WishDeletionView>() {

    suspend fun getWish(wishId: Long): Wish {
        return getWishUseCase.execute(wishId)
    }

    suspend fun deleteWish(wishId: Long): Boolean {
        try {
            deleteWishUseCase.execute(wishId)

            // update the rating on all wishes
            wishesRater.recalculateAndUpdateRatings(
                oldWishesCount = { wishesCount ->
                    wishesCount + 1
                }
            )

            return true
        } catch (e: WishNotDeletedException) {

        } catch (e: IllegalArgumentException) {

        }

        return false
    }

}
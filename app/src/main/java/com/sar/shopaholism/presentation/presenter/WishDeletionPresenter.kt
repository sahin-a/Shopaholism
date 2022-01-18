package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WishDeletionPresenter(
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishFeedbackService: WishFeedbackService
) : BasePresenter<WishDeletionView>() {

    override fun onAttachView() {
        super.onAttachView()

        CoroutineScope(Dispatchers.Default).launch {
            showWishDetails()
        }
    }

    private suspend fun showWishDetails() = coroutineScope {
        launch {
            var wish: Wish? = null

            try {
                wish = getWish()
            } catch (e: WishNotFoundException) {

            } catch (e: IllegalArgumentException) {

            }

            wish?.let {
                launch(Dispatchers.Main) {
                    view?.setWishData(it)
                }
            }
        }
    }

    private fun getWishId(): Long = view?.getWishId() ?: -1

    suspend fun getWish(): Wish {
        return getWishUseCase.execute(getWishId())
    }

    suspend fun deleteWish(): Boolean {
        try {
            deleteWishUseCase.execute(getWishId())
            wishFeedbackService.wishSuccessfullyDeleted()

            return true
        } catch (e: WishNotDeletedException) {

        } catch (e: IllegalArgumentException) {

        }

        return false
    }

}
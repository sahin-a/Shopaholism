package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishDeletionPresenter(
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishFeedbackService: WishFeedbackService
) : BasePresenter<WishDeletionView>() {

    override fun onAttachView() {
        super.onAttachView()

        CoroutineScope(Dispatchers.Default).launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        val wish = getWish()
        showWish(wish)
    }

    private suspend fun getWish(): Wish {
        return getWishUseCase.execute(getWishId())
    }

    private suspend fun showWish(wish: Wish) = withContext(Dispatchers.Main) {
        view?.setWishData(wish)
    }

    private fun getWishId(): Long = view?.getWishId() ?: -1

    suspend fun deleteWish() {
        try {
            deleteWishUseCase.execute(getWishId())
            wishFeedbackService.wishSuccessfullyDeleted()

            withContext(Dispatchers.Main) {
                view?.onSuccess()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                view?.onFailure()
            }
        }
    }

}

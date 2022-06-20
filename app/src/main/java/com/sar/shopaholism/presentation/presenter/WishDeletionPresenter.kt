package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.WishDeletionModel
import com.sar.shopaholism.presentation.view.WishDeletionView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WishDeletionPresenter(
    private val deleteWishUseCase: DeleteWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishFeedbackService: WishFeedbackService,
    private val model: WishDeletionModel
) : BasePresenter<WishDeletionView>() {

    override suspend fun onNewViewAttached() {
        super.onNewViewAttached()
        loadData()
    }

    override suspend fun onAttachView() {
        super.onAttachView()
        model.wish?.let { view?.setWishData(it) }
    }

    private suspend fun loadData() {
        getWish().let { model.wish = it }
    }

    private suspend fun getWish(): Wish {
        return getWishUseCase.execute(getWishId())
    }

    private fun getWishId(): Long = view?.getWishId() ?: -1

    suspend fun deleteWish(): Unit = coroutineScope {
        var success = false
        try {
            deleteWishUseCase.execute(getWishId())
            success = true
        } catch (e: Exception) {
        }

        launch(Dispatchers.Main) {
            if (!success) {
                view?.onFailure()
                return@launch
            }

            wishFeedbackService.wishSuccessfullyDeleted()
            view?.onSuccess()
        }
    }

}

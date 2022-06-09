package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishEditingPresenter(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishFeedbackService: WishFeedbackService
) : BasePresenter<WishEditingView>() {

    var wishId: Long? = null
    var originalWish: Wish? = null

    override suspend fun onNewViewAttached() {
        prefillWithOriginalData()
    }

    private suspend fun prefillWithOriginalData() {
        view?.getWishId()?.let { wishId ->
            getOriginalWish(wishId)?.apply {
                view?.setData(
                    title,
                    imageUri,
                    description,
                    price.toString()
                )
            }
        }
    }

    private suspend fun getOriginalWish(wishId: Long): Wish? {
        originalWish = try {
            getWishUseCase.execute(wishId)
        } catch (e: WishNotFoundException) {
            null
        }

        return originalWish
    }

    suspend fun updateWish(
        id: Long,
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ) = withContext(Dispatchers.Main) {
        updateWishUseCase.execute(
            Wish(
                id = id,
                title = title,
                imageUri = imageUri,
                description = description,
                price = price,
                priority = originalWish?.priority ?: 0.0
            )
        )
        wishFeedbackService.wishSuccessfullyModified()
    }
}
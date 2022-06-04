package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.*

class WishEditingPresenter(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishUseCase: GetWishUseCase,
    private val wishFeedbackService: WishFeedbackService,
    createWishModel: CreateWishModel
) : BaseWishCreationPresenter<WishEditingView>(createWishModel) {

    var wishId: Long? = null
    var originalWish: Wish? = null

    override fun onAttachView() {
        CoroutineScope(Dispatchers.Default).launch {
            prefillWithOriginalData()
        }
    }

    private suspend fun prefillWithOriginalData() {
        if (wishId == null || view?.getWishId() != wishId) {
            wishId = view?.getWishId()

            wishId?.let { wishId ->
                val originalWish = getOriginalWish(wishId)

                originalWish?.apply {
                    model.title = title
                    model.imageUri = imageUri
                    model.description = description
                    model.price = price
                }
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
package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishCreationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase,
    private val wishFeedbackService: WishFeedbackService,
    createWishModel: CreateWishModel
) : BaseWishCreationPresenter<WishCreationView>(createWishModel) {

    suspend fun createWish(
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ): Boolean = withContext(Dispatchers.Main) {

        try {
            createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )
            wishFeedbackService.wishSuccessfullyCreated()

            return@withContext true
        } catch (e: WishNotCreatedException) {

        } catch (e: IllegalArgumentException) {

        }

        return@withContext false
    }

    companion object {
        const val TAG = "WishCreationPresenter"
    }
}

package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.view.WishCreationView

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
    ): Boolean {

        try {
            createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )
            wishFeedbackService.wishSuccessfullyCreated()

            return true
        } catch (e: WishNotCreatedException) {

        } catch (e: IllegalArgumentException) {

        }

        return false
    }

    companion object {
        const val TAG = "WishCreationPresenter"
    }

}
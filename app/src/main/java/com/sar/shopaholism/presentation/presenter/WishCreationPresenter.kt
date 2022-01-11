package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.exception.WishNotCreatedException
import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.rater.WishesRater
import com.sar.shopaholism.presentation.view.WishCreationView

class WishCreationPresenter(
    private val createWishUseCase: CreateWishUseCase,
    private val wishesRater: WishesRater,
    private val wishFeedbackService: WishFeedbackService
) : BaseWishCreationPresenter<WishCreationView>() {

    suspend fun createWish(
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ): Boolean {

        try {
            val id = createWishUseCase.execute(
                title = title,
                imageUri = imageUri,
                description = description,
                price = price
            )

            // update the rating on all wishes
            wishesRater.recalculateAndUpdateRatings(
                oldWishesCount = { wishesCount ->
                    wishesCount - 1
                }
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
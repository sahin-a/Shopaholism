package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.runBlocking

class WishEditingPresenter(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishUseCase: GetWishUseCase
) : BaseWishCreationPresenter<WishEditingView>() {

    private var wishId: Long? = null

    override fun onAttachView() {
        view?.let { view ->
            runBlocking {
                if (wishId == null || view.getWishId() != wishId) {
                    wishId = view.getWishId()

                    wishId?.let { wishId ->
                        val originalWish = getWishUseCase.execute(wishId)

                        originalWish.apply {
                            model.title = title
                            model.imageUri = imageUri
                            model.description = description
                            model.price = price
                        }
                    }
                }
            }
        }
    }

    private suspend fun getOriginalWish(wishId: Long): Wish {
        // TODO: Exception handling
        return getWishUseCase.execute(wishId)
    }

    suspend fun updateWish(
        id: Long,
        title: String,
        imageUri: String,
        description: String,
        price: Double
    ) {
        updateWishUseCase.execute(
            Wish(
                id = id,
                title = title,
                imageUri = imageUri,
                description = description,
                price = price,
                priority = 0
            )
        )
    }
}
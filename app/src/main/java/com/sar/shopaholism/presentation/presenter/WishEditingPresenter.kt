package com.sar.shopaholism.presentation.presenter

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.usecase.GetWishUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import com.sar.shopaholism.presentation.view.WishEditingView
import kotlinx.coroutines.runBlocking

class WishEditingPresenter(
    private val updateWishUseCase: UpdateWishUseCase,
    private val getWishUseCase: GetWishUseCase
) : BaseWishCreationPresenter<WishEditingView>() {

    var wishId: Long? = null
    var originalWish: Wish? = null

    override fun onAttachView() {
        view?.let { view ->
            runBlocking {
                if (wishId == null || view.getWishId() != wishId) {
                    wishId = view.getWishId()

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
    ) {
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
    }
}
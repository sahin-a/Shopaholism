package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.domain.repository.WishesRepository

class UpdateWishUseCase(
    private val repo: WishesRepository,
    private val logger: Logger
) {

    suspend fun execute(wish: Wish) {
        WishValidation.validate(
            wishId = wish.id,
            title = wish.title,
            price = wish.price,
            priority = wish.priority
        )

        val isValueUpdated = repo.update(wish)

        if (!isValueUpdated) {
            logger.d(TAG, "Wish couldn't be updated")
            throw WishNotUpdatedException("Wish couldn't be updated")
        }

        logger.d(TAG, "Wish has been updated")
    }

    companion object {
        const val TAG: String = "UpdateWishUseCase"
    }
}
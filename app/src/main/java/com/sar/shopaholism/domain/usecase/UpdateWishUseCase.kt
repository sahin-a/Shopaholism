package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.domain.repository.WishesRepository

class UpdateWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wish: Wish) {
        WishValidation.validate(
            wishId = wish.id,
            imageUri = wish.imageUri,
            title = wish.title,
            description = wish.description,
            price = wish.price,
            priority = wish.priority
        )

        val isValueUpdated = repo.update(wish)

        if (!isValueUpdated) {
            throw WishNotUpdatedException("Wish couldn't be updated")
        }
    }
}
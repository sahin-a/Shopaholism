package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository

class DeleteWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wishId: Long) {
        WishValidation.validate(wishId = wishId)

        val isDeleted = repo.delete(wishId)

        if (!isDeleted) {
            throw WishNotDeletedException("Wish couldn't be deleted")
        }
    }
}
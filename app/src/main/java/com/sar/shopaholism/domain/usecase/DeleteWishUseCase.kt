package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository

class DeleteWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wishId: Long) {
        require(wishId > 0) { "Wish doesn't have a valid Id" }

        val isDeleted = repo.delete(wishId)

        if (isDeleted) {
            throw WishNotDeletedException("Wish couldn't be deleted")
        }
    }
}
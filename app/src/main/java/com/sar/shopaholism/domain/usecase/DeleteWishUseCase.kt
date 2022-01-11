package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.exception.WishNotDeletedException
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wishId: Long) = withContext(Dispatchers.IO) {
        WishValidation.validate(wishId = wishId)

        val isDeleted = repo.delete(wishId)

        if (!isDeleted) {
            throw WishNotDeletedException("Wish couldn't be deleted")
        }
    }
}
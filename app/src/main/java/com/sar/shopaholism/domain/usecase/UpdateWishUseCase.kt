package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.domain.repository.WishesRepository

class UpdateWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wish: Wish) {
        require(wish != null) { "Wish can't be null" }

        val isValueUpdated = repo.update(wish)

        if (!isValueUpdated) {
            throw WishNotUpdatedException("Wish couldn't be updated")
        }
    }
}
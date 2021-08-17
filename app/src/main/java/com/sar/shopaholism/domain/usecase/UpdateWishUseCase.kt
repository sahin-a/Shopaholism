package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotUpdatedException
import com.sar.shopaholism.domain.repository.WishesRepository

class UpdateWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(
        wishId: Long,
        title: String,
        description: String,
        price: Double,
        priority: Int
    ) {
        require(wishId > 0) { "Wish doesn't have a valid Id" }

        val wish: Wish = Wish(wishId, title, description, price, priority)
        val isValueUpdated = repo.update(wish)

        if (!isValueUpdated) {
            throw WishNotUpdatedException("Wish couldn't be updated")
        }
    }
}
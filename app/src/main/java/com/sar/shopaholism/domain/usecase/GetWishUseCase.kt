package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.repository.WishesRepository

class GetWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wishId: Long): Wish {
        WishValidation.validate(wishId = wishId)

        try {
            return repo.getWish(wishId)
        } catch (exception: NullPointerException) {
            throw WishNotFoundException("Couldn't find a wish with the corresponding id")
        }
    }
}
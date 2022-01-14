package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.exception.WishNotFoundException
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWishUseCase(private val repo: WishesRepository) {

    suspend fun execute(wishId: Long, dispatcher: CoroutineDispatcher = Dispatchers.IO): Wish =
        withContext(dispatcher) {
            WishValidation.validate(wishId = wishId)

            try {
                return@withContext repo.getWish(wishId)
            } catch (exception: NullPointerException) {
                throw WishNotFoundException("Couldn't find a wish with the corresponding id")
            }
        }
}
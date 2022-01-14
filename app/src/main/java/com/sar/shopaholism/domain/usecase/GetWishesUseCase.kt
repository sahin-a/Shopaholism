package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetWishesUseCase(private val repo: WishesRepository) {

    suspend fun execute(dispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<List<Wish>> =
        withContext(dispatcher) {
            return@withContext repo.getWishes()
        }
}
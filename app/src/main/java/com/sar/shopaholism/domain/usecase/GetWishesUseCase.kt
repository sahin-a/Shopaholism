package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetWishesUseCase(private val repo: WishesRepository) {

    suspend fun execute(): Flow<List<Wish>> = withContext(Dispatchers.IO) {
        return@withContext repo.getWishes()
    }
}
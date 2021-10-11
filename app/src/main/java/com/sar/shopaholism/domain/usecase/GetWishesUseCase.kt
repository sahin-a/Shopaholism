package com.sar.shopaholism.domain.usecase

import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.flow.Flow

class GetWishesUseCase(private val repo: WishesRepository) {

    suspend fun execute(): Flow<List<Wish>> {
        return repo.getWishes()
    }
}
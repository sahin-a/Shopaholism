package com.sar.shopaholism.domain.repository

import com.sar.shopaholism.domain.entity.Wish
import kotlinx.coroutines.flow.Flow

interface WishesRepository {
    suspend fun getWishes(): Flow<List<Wish>>

    suspend fun create(wish: Wish): Long

    suspend fun delete(wishId: Long): Boolean
}
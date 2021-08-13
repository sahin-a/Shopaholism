package com.sar.shopaholism.data.repository

import com.sar.shopaholism.data.local.mapper.toWishEntity
import com.sar.shopaholism.data.local.mapper.toWishes
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.domain.entity.Wish
import com.sar.shopaholism.domain.repository.WishesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishesRepositoryImpl(private val dataSource: WishesDataSource) : WishesRepository {

    override suspend fun getWishes(): Flow<List<Wish>> =
        dataSource.getWishes().map { it.toWishes() }

    override suspend fun create(wish: Wish): Long =
        dataSource.insert(wish.toWishEntity())

    override suspend fun delete(wishId: Long): Boolean =
        dataSource.deleteWish(wishId)

    override suspend fun update(wish: Wish): Boolean =
        dataSource.updateWish(wish.toWishEntity())
}
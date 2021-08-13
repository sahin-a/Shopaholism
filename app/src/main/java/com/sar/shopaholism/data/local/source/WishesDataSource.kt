package com.sar.shopaholism.data.local.source

import com.sar.shopaholism.data.local.dao.WishDao
import com.sar.shopaholism.data.local.entity.WishEntity
import kotlinx.coroutines.flow.Flow

class WishesDataSource(private val dao: WishDao) {
    fun getWishes(): Flow<List<WishEntity>> = dao.getAll()

    suspend fun insert(wish: WishEntity): Long = dao.insert(wish)

    suspend fun getWish(wishId: Long): WishEntity = dao.getById(wishId)

    suspend fun deleteWish(wishId: Long): Boolean = dao.deleteById(wishId) > 0

    suspend fun updateWish(wish: WishEntity): Boolean = dao.update(wish) > 0
}
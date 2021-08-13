package com.sar.shopaholism.data.local.dao

import androidx.room.*
import com.sar.shopaholism.data.local.entity.WishEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WishDao {
    /* All */
    @Query("SELECT * FROM wishes")
    abstract fun getAll(): Flow<List<WishEntity>>

    @Query("DELETE FROM wishes")
    abstract suspend fun deleteAll(): Int

    /* by Id */
    @Query("SELECT * FROM wishes WHERE id = :id")
    abstract suspend fun getById(id: Long): WishEntity

    @Query("DELETE FROM wishes WHERE id = :id")
    abstract suspend fun deleteById(id: Long): Int

    /* by Object */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(wish: WishEntity): Long

    @Delete
    abstract suspend fun delete(wish: WishEntity): Int

    @Update
    abstract suspend fun update(wish: WishEntity): Int
}
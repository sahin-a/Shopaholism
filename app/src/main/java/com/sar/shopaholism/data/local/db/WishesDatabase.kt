package com.sar.shopaholism.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sar.shopaholism.data.local.dao.WishDao
import com.sar.shopaholism.data.local.entity.WishEntity

@Database(
    version = 1,
    entities = [WishEntity::class]
)
abstract class WishesDatabase : RoomDatabase() {

    abstract fun wishDao(): WishDao

    companion object {
        const val DATABASE_NAME = "shopaholism.db"
    }
}
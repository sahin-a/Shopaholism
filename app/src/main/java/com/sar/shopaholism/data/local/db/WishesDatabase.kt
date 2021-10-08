package com.sar.shopaholism.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sar.shopaholism.data.local.dao.WishDao
import com.sar.shopaholism.data.local.entity.WishEntity

@Database(
    version = 2,
    entities = [WishEntity::class]
)
abstract class WishesDatabase : RoomDatabase() {

    abstract fun wishDao(): WishDao

    companion object {
        const val DATABASE_NAME = "shopaholism.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                // Create the new table
                database.execSQL(
                    "CREATE TABLE wishes_temp (id INTEGER NOT NULL, image_uri TEXT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, price REAL NOT NULL, priority REAL NOT NULL, PRIMARY KEY(id))"
                )

                // Copy the data
                database.execSQL(
                    "INSERT INTO wishes_temp (id, image_uri, title, description, price, priority) SELECT id, image_uri, title, description, price, priority FROM wishes"
                )

                // Remove the old table
                database.execSQL("DROP TABLE wishes")

                // Change the table name to the correct one
                database.execSQL("ALTER TABLE wishes_temp RENAME TO wishes")
            }
        }
    }

}
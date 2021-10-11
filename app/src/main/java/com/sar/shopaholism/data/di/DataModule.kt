package com.sar.shopaholism.data.di

import androidx.room.Room
import com.sar.shopaholism.data.local.dao.WishDao
import com.sar.shopaholism.data.local.db.WishesDatabase
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.data.repository.WishesRepositoryImpl
import com.sar.shopaholism.domain.repository.WishesRepository
import org.koin.dsl.module

private val databaseModule = module {
    single {
        Room.databaseBuilder(get(), WishesDatabase::class.java, WishesDatabase.DATABASE_NAME)
            .addMigrations(WishesDatabase.MIGRATION_1_2)
            .build()
    }
    single<WishDao> { get<WishesDatabase>().wishDao() }
}

private val repositoryModule = module {
    single { WishesDataSource(get()) }
    single<WishesRepository> { WishesRepositoryImpl(get()) }
}

val dataModules = databaseModule + repositoryModule
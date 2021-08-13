package com.sar.shopaholism.data.di

import androidx.room.Room
import com.sar.shopaholism.data.local.db.WishesDatabase
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.data.repository.WishesRepositoryImpl
import com.sar.shopaholism.domain.repository.WishesRepository
import org.koin.dsl.module

private val databaseModule = module {
    single {
        Room.databaseBuilder(get(), WishesDatabase::class.java, WishesDatabase.DATABASE_NAME)
    }
    single { get<WishesDatabase>().wishDao() }
}

private val repositoryModule = module {
    single<WishesRepository> { WishesRepositoryImpl(get()) }
    single { WishesDataSource(get()) }
}

val dataModules = databaseModule + repositoryModule
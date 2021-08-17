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
            .build()
    }
    single { get<WishesDatabase>().wishDao() }
}

private val repositoryModule = module {
    single { WishesDataSource(get()) }
    single<WishesRepository> { WishesRepositoryImpl(get()) }
}

val dataModules = databaseModule + repositoryModule
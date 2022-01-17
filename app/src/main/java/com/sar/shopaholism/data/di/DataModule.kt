package com.sar.shopaholism.data.di

import androidx.room.Room
import com.github.kittinunf.fuel.core.FuelManager
import com.sar.shopaholism.data.local.db.WishesDatabase
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.data.repository.WishesRepositoryImpl
import com.sar.shopaholism.data.web.WebApiClient
import com.sar.shopaholism.data.web.WebApiClientImpl
import com.sar.shopaholism.data.wikipedia.WikipediaClient
import com.sar.shopaholism.data.wikipedia.api.WikipediaApiProvider
import com.sar.shopaholism.domain.repository.WishesRepository
import org.koin.dsl.module

private val databaseModule = module {
    single {
        Room.databaseBuilder(get(), WishesDatabase::class.java, WishesDatabase.DATABASE_NAME)
            .addMigrations(WishesDatabase.MIGRATION_1_2)
            .build()
    }
    single { get<WishesDatabase>().wishDao() }
}

private val apiModule = module {
    factory { FuelManager() }
    factory<WebApiClient> {
        WebApiClientImpl(
            fuelManager = get(),
            logger = get(),
            rateLimiter = null
        )
    }
    single { WikipediaApiProvider(get()) }
    single { WikipediaClient(get()) }
}

private val repositoryModule = module {
    single { WishesDataSource(dao = get()) }

    single<WishesRepository> { WishesRepositoryImpl(dataSource = get()) }
}

val dataModules = databaseModule + apiModule + repositoryModule
package com.sar.shopaholism.data.di

import androidx.room.Room
import com.github.kittinunf.fuel.core.FuelManager
import com.sar.shopaholism.data.local.dao.WishDao
import com.sar.shopaholism.data.local.db.WishesDatabase
import com.sar.shopaholism.data.local.source.WishesDataSource
import com.sar.shopaholism.data.remote.productlookup.BarcodeLookupApi
import com.sar.shopaholism.data.remote.productlookup.RateLimiter
import com.sar.shopaholism.data.remote.productlookup.source.BarcodeLookupDataSourceImpl
import com.sar.shopaholism.data.remote.productlookup.source.ProductLookupDataSource
import com.sar.shopaholism.data.repository.ProductLookupRepositoryImpl
import com.sar.shopaholism.data.repository.WishesRepositoryImpl
import com.sar.shopaholism.domain.repository.ProductLookupRepository
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

private val apiModule = module {
    factory { FuelManager() }
    factory { RateLimiter() }
    single { BarcodeLookupApi(fuelManager = get(), rateLimiter = get()) }
}

private val repositoryModule = module {
    single { WishesDataSource(dao = get()) }
    single<ProductLookupDataSource> { BarcodeLookupDataSourceImpl(apiClient = get()) }

    single<WishesRepository> { WishesRepositoryImpl(dataSource = get()) }
    single<ProductLookupRepository> { ProductLookupRepositoryImpl(dataSource = get()) }
}

val dataModules = databaseModule + apiModule + repositoryModule
package com.sar.shopaholism.domain.di

import com.sar.shopaholism.domain.usecase.*
import com.sar.shopaholism.domain.usecase.productlookup.ProductLookupUseCase
import org.koin.dsl.module

private val modules = module {
    single { CreateWishUseCase(get(), logger = get()) }
    single { DeleteWishUseCase(get()) }
    single { GetWishesUseCase(get()) }
    single { UpdateWishUseCase(get(), logger = get()) }
    single { GetWishUseCase(get()) }
    single { ProductLookupUseCase(productLookupRepository = get(), logger = get()) }
}

val domainModules = listOf(modules)
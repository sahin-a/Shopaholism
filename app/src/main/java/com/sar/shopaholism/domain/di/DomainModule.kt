package com.sar.shopaholism.domain.di

import com.sar.shopaholism.domain.usecase.*
import org.koin.dsl.module

private val modules = module {
    single { CreateWishUseCase(get(), logger = get()) }
    single { DeleteWishUseCase(get()) }
    single { GetWishesUseCase(get()) }
    single { UpdateWishUseCase(get(), logger = get()) }
    single { GetWishUseCase(get()) }
    single { GetWikiPageUseCase(get(), logger = get()) }
}

val domainModules = listOf(modules)
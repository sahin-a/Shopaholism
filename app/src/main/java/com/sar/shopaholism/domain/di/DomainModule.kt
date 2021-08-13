package com.sar.shopaholism.domain.di

import com.sar.shopaholism.domain.usecase.CreateWishUseCase
import com.sar.shopaholism.domain.usecase.DeleteWishUseCase
import com.sar.shopaholism.domain.usecase.GetWishesUseCase
import com.sar.shopaholism.domain.usecase.UpdateWishUseCase
import org.koin.dsl.module

private val modules = module {
    single { CreateWishUseCase(get()) }
    single { DeleteWishUseCase(get()) }
    single { GetWishesUseCase(get()) }
    single { UpdateWishUseCase(get()) }
}

val domainModules = listOf(modules)
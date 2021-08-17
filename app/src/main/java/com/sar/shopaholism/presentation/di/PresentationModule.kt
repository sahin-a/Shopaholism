package com.sar.shopaholism.presentation.di

import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import org.koin.dsl.module

private val modules = module {
    factory { WishesOverviewPresenter(getWishesUseCase = get(), createWishUseCase = get()) }
}

val presentationModules = listOf(modules)
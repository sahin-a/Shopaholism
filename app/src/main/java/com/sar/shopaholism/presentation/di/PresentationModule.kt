package com.sar.shopaholism.presentation.di

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.presentation.logger.LoggerImpl
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.model.SortWishModel
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.presenter.*
import org.koin.dsl.module

private val modules = module {
    single<Logger> { LoggerImpl() }

    // Models
    factory { WishesModel() }
    factory { CreateWishModel() }
    factory { SortWishModel() }

    // Presenters
    single { WishesOverviewPresenter(getWishesUseCase = get(), model = get(), logger = get()) }
    single { WishCreationPresenter(createWishUseCase = get()) }
    single { WishEditingPresenter(updateWishUseCase = get(), getWishUseCase = get()) }
    single { WishDeletionPresenter(deleteWishUseCase = get(), getWishUseCase = get()) }
    single {
        WishSortPresenter(
            getWishUseCase = get(),
            updateWishUseCase = get(),
            getWishesUseCase = get(),
            model = get(),
            logger = get()
        )
    }
}

val presentationModules = listOf(modules)
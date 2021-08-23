package com.sar.shopaholism.presentation.di

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.presentation.logger.LoggerImpl
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.presenter.WishCreationPresenter
import com.sar.shopaholism.presentation.presenter.WishEditingPresenter
import com.sar.shopaholism.presentation.presenter.WishesOverviewPresenter
import org.koin.dsl.module

private val modules = module {
    single<Logger> { LoggerImpl() }

    single { WishesOverviewPresenter(getWishesUseCase = get(), model = get(), logger = get()) }
    single { WishCreationPresenter(createWishUseCase = get()) }
    single { WishEditingPresenter(updateWishUseCase = get(), getWishUseCase = get()) }
    single { WishesModel() }
}

val presentationModules = listOf(modules)
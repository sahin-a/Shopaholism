package com.sar.shopaholism.presentation.di

import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.presentation.logger.LoggerImpl
import com.sar.shopaholism.presentation.model.CreateWishModel
import com.sar.shopaholism.presentation.model.SortWishModel
import com.sar.shopaholism.presentation.model.WishesModel
import com.sar.shopaholism.presentation.presenter.*
import com.sar.shopaholism.presentation.rater.WishesRater
import com.squareup.picasso.Picasso
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val modules = module {
    single<Logger> { LoggerImpl() }

    // Models
    factory { WishesModel() }
    factory { CreateWishModel() }
    factory { SortWishModel() }

    // Presenters
    single {
        WishesOverviewPresenter(
            getWishesUseCase = get(),
            model = get(),
            logger = get()
        )
    }

    single {
        WishCreationPresenter(
            createWishUseCase = get(),
            wishesRater = get(),
            logger = get()
        )
    }

    single {
        WishEditingPresenter(
            updateWishUseCase = get(),
            getWishUseCase = get()
        )
    }

    single {
        WishDeletionPresenter(
            deleteWishUseCase = get(),
            getWishUseCase = get(),
            wishesRater = get()
        )
    }

    single {
        WishSortPresenter(
            getWishUseCase = get(),
            updateWishUseCase = get(),
            getWishesUseCase = get(),
            model = get(),
            logger = get()
        )
    }

    single {
        WishesRater(
            updateWishUseCase = get(),
            getWishesUseCase = get()
        )
    }

    single {
        WishDetailPresenter(
            getWishUseCase = get(),
            getProductLookupUseCase = get(),
            logger = get()
        )
    }

    single<Picasso> {
        Picasso
            .Builder(androidContext())
            .build()
    }

}

val presentationModules = listOf(modules)
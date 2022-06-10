package com.sar.shopaholism.presentation.di

import android.media.MediaPlayer
import com.sar.shopaholism.domain.logger.Logger
import com.sar.shopaholism.presentation.feedback.SoundProvider
import com.sar.shopaholism.presentation.feedback.SoundProviderImpl
import com.sar.shopaholism.presentation.feedback.WishFeedbackService
import com.sar.shopaholism.presentation.logger.LoggerImpl
import com.sar.shopaholism.presentation.model.*
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
    factory { WishDetailModel() }
    factory { WishDeletionModel() }

    // Presenters
    single {
        WishesOverviewPresenter(
            getWishesUseCase = get(),
            model = get(),
            logger = get()
        )
    }

    factory {
        WishCreationPresenter(
            createWishUseCase = get(),
            wishFeedbackService = get()
        )
    }

    factory {
        WishEditingPresenter(
            updateWishUseCase = get(),
            getWishUseCase = get(),
            wishFeedbackService = get()
        )
    }

    single {
        WishDeletionPresenter(
            deleteWishUseCase = get(),
            getWishUseCase = get(),
            wishFeedbackService = get(),
            model = get()
        )
    }

    single {
        WishSortPresenter(get(), get(), get(), get(), get(), get())
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
            logger = get(),
            getWikiPageUseCase = get(),
            model = get()
        )
    }

    single<Picasso> {
        Picasso
            .Builder(androidContext())
            .build()
    }

    single { MediaPlayer() }

    single<SoundProvider> { SoundProviderImpl(context = androidContext(), mediaPlayer = get()) }
    single { WishFeedbackService(soundProvider = get()) }
}

val presentationModules = listOf(modules)
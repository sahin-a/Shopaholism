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
    single { WishesRater(get(), get()) }
    single<Picasso> {
        Picasso
            .Builder(androidContext())
            .build()
    }
    single { MediaPlayer() }
    single<SoundProvider> { SoundProviderImpl(context = androidContext(), mediaPlayer = get()) }
    single { WishFeedbackService(soundProvider = get()) }

    factory { WishesModel() }
    factory { CreateWishModel() }
    factory { SortWishModel() }
    factory { WishDetailModel() }
    factory { WishDeletionModel() }

    factory { WishesOverviewPresenter(get(), get(), get()) }
    factory { WishCreationPresenter(get(), get()) }
    factory { WishEditingPresenter(get(), get(), get()) }
    factory { WishDeletionPresenter(get(), get(), get(), get()) }
    factory { WishSortPresenter(get(), get(), get(), get(), get(), get()) }
    factory { WishDetailPresenter(get(), get(), get(), get(), get()) }
}
val presentationModules = listOf(modules)

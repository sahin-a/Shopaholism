package com.sar.shopaholism

import android.app.Application
import com.sar.shopaholism.data.di.dataModules
import com.sar.shopaholism.domain.di.domainModules
import com.sar.shopaholism.presentation.di.presentationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(domainModules + dataModules + presentationModules)
        }
    }

}
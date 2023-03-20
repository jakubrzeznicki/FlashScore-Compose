package com.kuba.flashscorecompose

import android.app.Application
import com.kuba.flashscorecompose.di.KoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FlashScoreComposeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@FlashScoreComposeApp)
            modules(KoinModules().getAllModules())
        }
    }
}

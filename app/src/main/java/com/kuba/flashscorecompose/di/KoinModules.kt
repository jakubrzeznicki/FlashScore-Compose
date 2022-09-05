package com.kuba.flashscorecompose.di

import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
    }
    private val networkModule = module {
        //single<NetworkControllerInterface> { NetworkController(androidContext()) }
    }

    fun getAllModules() = listOf(
        componentsModule, networkModule
    )
}
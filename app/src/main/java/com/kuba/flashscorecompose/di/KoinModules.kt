package com.kuba.flashscorecompose.di

import com.kuba.flashscorecompose.data.FlashScoreApi
import com.kuba.flashscorecompose.data.FlashScoreApi.Companion.BASE_URL
import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
    }
    private val networkModule = module {
        single<FlashScoreApi> {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(FlashScoreApi::class.java)
        }
    }

    fun getAllModules() = listOf(
        componentsModule, networkModule
    )
}
package com.example.network.di

import com.example.network.api.FootballApi
import com.example.network.provider.DefaultApiProvider
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Created by jrzeznicki on 9/9/2022
 */
val networkModule = module {
    single { provideRetrofit() }
    factory { provideApi(get()) }
}

private fun provideRetrofit(): Retrofit {
    return DefaultApiProvider().setupRetrofit()
}

private fun provideApi(retrofit: Retrofit): FootballApi = retrofit.create(FootballApi::class.java)

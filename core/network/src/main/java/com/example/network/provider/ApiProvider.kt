package com.example.network.provider

import retrofit2.Retrofit

/**
 * Created by jrzeznicki on 20/12/2022.
 */
interface ApiProvider {
    fun setupRetrofit(): Retrofit
}

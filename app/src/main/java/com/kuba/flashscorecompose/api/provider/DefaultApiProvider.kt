package com.kuba.flashscorecompose.api.provider

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by jrzeznicki on 21/12/2022.
 */
class DefaultApiProvider : ApiProvider {
    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }
    private val headerInterceptor: Interceptor
        get() {
            val header = Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header(KEY_NAME, BuildConfig.API_KEY)
                builder.header(HOST_NAME, HOST_VALUE)
                return@Interceptor chain.proceed(builder.build())
            }
            return header
        }

    override fun setupRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                addInterceptor(headerInterceptor)
                addNetworkInterceptor(loggingInterceptor)
            }
            .build()
    }

    private companion object {
        const val BASE_URL = "https://api-football-v1.p.rapidapi.com/"
        const val KEY_NAME = "X-RapidAPI-Key"
        const val HOST_NAME = "X-RapidAPI-Host"
        const val HOST_VALUE = "api-football-v1.p.rapidapi.com"
    }
}
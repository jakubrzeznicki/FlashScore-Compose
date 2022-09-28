package com.kuba.flashscorecompose.di

import com.kuba.flashscorecompose.countries.viewmodel.CountriesViewModel
import com.kuba.flashscorecompose.data.LocalRoomStorage
import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.CountryRepository
import com.kuba.flashscorecompose.data.country.local.CountryLocal
import com.kuba.flashscorecompose.data.country.remote.CountryRemote
import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {

    private val viewModelsModule = module {
        viewModel { CountriesViewModel(get()) }
    }

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
    }

    private val repositoryModule = module {
        single<CountryDataSource> {
            val local = CountryLocal(get())
            val remote = CountryRemote(get())
            CountryRepository(local, remote)
        }
    }

    private val storageModule = module {
        single<RoomStorage> {
            LocalRoomStorage(androidApplication())
        }
    }

    fun getAllModules() = listOf(
        componentsModule,
        storageModule,
        networkModule,
        repositoryModule,
        viewModelsModule
    )
}
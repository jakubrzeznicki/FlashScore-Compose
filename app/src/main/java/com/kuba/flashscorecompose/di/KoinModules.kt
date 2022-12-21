package com.kuba.flashscorecompose.di

import com.kuba.flashscorecompose.countries.viewmodel.CountriesViewModel
import com.kuba.flashscorecompose.data.LocalRoomStorage
import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.country.CountryDataSource
import com.kuba.flashscorecompose.data.country.CountryRepository
import com.kuba.flashscorecompose.data.country.local.CountryLocal
import com.kuba.flashscorecompose.data.country.remote.CountryRemote
import com.kuba.flashscorecompose.data.league.LeagueDataSource
import com.kuba.flashscorecompose.data.league.LeagueRepository
import com.kuba.flashscorecompose.data.league.local.LeagueLocal
import com.kuba.flashscorecompose.data.league.remote.LeagueRemote
import com.kuba.flashscorecompose.leagues.viewmodel.LeaguesViewModel
import com.kuba.flashscorecompose.network.uuidsource.UuidData
import com.kuba.flashscorecompose.network.uuidsource.UuidSource
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate
import java.util.Calendar

/**
 * Created by jrzeznicki on 9/5/2022
 */
class KoinModules {

    private val viewModelsModule = module {
        viewModel { CountriesViewModel(get()) }
        viewModel { (countryCode: String) -> LeaguesViewModel(countryCode, get(), get()) }
    }

    private val componentsModule = module {
        single<UuidSource> { UuidData() }
        single<LocalDate> { LocalDate.now() }
    }

    private val repositoryModule = module {
        single<CountryDataSource> {
            val local = CountryLocal(get())
            val remote = CountryRemote(get())
            CountryRepository(local, remote)
        }
        single<LeagueDataSource> {
            val local = LeagueLocal(get())
            val remote = LeagueRemote(get())
            LeagueRepository(local, remote)
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
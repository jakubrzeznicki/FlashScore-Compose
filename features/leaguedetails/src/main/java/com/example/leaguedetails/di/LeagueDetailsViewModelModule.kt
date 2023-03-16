package com.example.leaguedetails.di

import com.example.favoritefixtureinteractor.DefaultFavoriteFixtureInteractor
import com.example.leaguedetails.viewmodel.LeagueDetailsViewModel
import com.example.model.league.League
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val leagueDetailsViewModelModule = module {
    viewModel { (league: League) ->
        val favoriteFixtureInteractor = DefaultFavoriteFixtureInteractor(get(), get(), get())
        LeagueDetailsViewModel(league, get(), get(), get(), favoriteFixtureInteractor)
    }
}
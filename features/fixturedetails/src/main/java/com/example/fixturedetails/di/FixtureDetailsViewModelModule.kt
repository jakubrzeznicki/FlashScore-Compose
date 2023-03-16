package com.example.fixturedetails.di

import com.example.favoritefixtureinteractor.DefaultFavoriteFixtureInteractor
import com.example.fixturedetails.container.viewmodel.FixtureDetailsViewModel
import com.example.fixturedetails.headtohead.viewmodel.HeadToHeadViewModel
import com.example.fixturedetails.lineup.viewmodel.LineupViewModel
import com.example.fixturedetails.statistics.viewmodel.StatisticsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val fixtureDetailsViewModelModule = module {
    viewModel { (fixtureId: Int) -> FixtureDetailsViewModel(fixtureId, get(), get()) }
    viewModel { (fixtureId: Int, leagueId: Int, round: String, season: Int) ->
        val favoriteFixtureInteractor = DefaultFavoriteFixtureInteractor(get(), get(), get())
        StatisticsViewModel(
            fixtureId,
            leagueId,
            round,
            season,
            get(),
            get(),
            get(),
            favoriteFixtureInteractor,
            get()
        )
    }
    viewModel { (fixtureId: Int, leagueId: Int, season: Int) ->
        LineupViewModel(
            fixtureId,
            leagueId,
            season,
            get(),
            get()
        )
    }
    viewModel { (homeTeamId: Int, awayTeamId: Int, season: Int) ->
        HeadToHeadViewModel(
            homeTeamId,
            awayTeamId,
            season,
            get(),
            get()
        )
    }
}
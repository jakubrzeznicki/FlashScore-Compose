package com.example.standingsdetails.di

import com.example.standingsdetails.viewmodel.StandingsDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val standingsDetailsViewModelModule = module {
    viewModel { (leagueId: Int, season: Int) ->
        StandingsDetailsViewModel(
            leagueId,
            season,
            get(),
            get()
        )
    }
}
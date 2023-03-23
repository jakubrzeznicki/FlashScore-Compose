package com.example.standings.di

import com.example.standings.viewmodel.StandingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
/**
 * Created by jrzeznicki on 15/03/2023.
 */
val standingsViewModelModule = module {
    viewModel { StandingsViewModel(get(), get(), get(), get()) }
}
















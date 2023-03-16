package com.example.playerdetails.di

import com.example.model.team.Team
import com.example.playerdetails.viewmodel.PlayerDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by jrzeznicki on 15/03/2023.
 */
val playerDetailsViewModelModule = module {
    viewModel { (playerId: Int, team: Team, season: Int) ->
        PlayerDetailsViewModel(
            playerId,
            team,
            season,
            get(),
            get(),
            get()
        )
    }
}
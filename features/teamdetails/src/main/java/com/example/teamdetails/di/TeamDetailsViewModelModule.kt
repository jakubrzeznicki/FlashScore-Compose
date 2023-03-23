package com.example.teamdetails.di

/**
 * Created by jrzeznicki on 15/03/2023.
 */
import com.example.favoritefixtureinteractor.DefaultFavoriteFixtureInteractor
import com.example.model.team.Team
import com.example.teamdetails.fixturesteam.viewmodel.FixturesTeamViewModel
import com.example.teamdetails.informations.viewmodel.TeamInformationsViewModel
import com.example.teamdetails.players.viewmodel.PlayersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val teamDetailsViewModelModule = module {
    viewModel { (team: Team, leagueId: Int, season: Int) ->
        TeamInformationsViewModel(team, leagueId, season, get(), get(), get())
    }
    viewModel { (team: Team, season: Int) ->
        PlayersViewModel(
            team,
            season,
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { (teamId: Int, season: Int) ->
        val favoriteFixtureInteractor = DefaultFavoriteFixtureInteractor(get(), get(), get())
        FixturesTeamViewModel(
            teamId,
            season,
            get(),
            get(),
            favoriteFixtureInteractor,
            get()
        )
    }
}
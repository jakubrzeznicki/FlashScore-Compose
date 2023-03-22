package com.example.leaguedetails.navigation

import com.example.model.league.League

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface LeagueDetailsNavigator {
    fun openFixtureDetails(fixtureId: Int)
    fun openStandingsDetails(league: League)
    fun navigateUp()
}
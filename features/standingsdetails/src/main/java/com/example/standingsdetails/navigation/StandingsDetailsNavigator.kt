package com.example.standingsdetails.navigation

import com.example.model.league.League
import com.example.model.team.Team

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface StandingsDetailsNavigator {
    fun openTeamDetails(team: Team, leagueId: Int, season: Int)
    fun openLeagueDetails(league: League)
    fun navigateUp()
}
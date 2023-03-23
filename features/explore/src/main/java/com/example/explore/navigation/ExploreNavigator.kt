package com.example.explore.navigation

import com.example.model.league.League
import com.example.model.team.Team

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface ExploreNavigator {
    fun openFixtureDetails(fixtureId: Int)
    fun openTeamDetails(team: Team, leagueId: Int, season: Int)
    fun openPlayerDetails(playerId: Int, team: Team, season: Int)
    fun openLeagueDetails(league: League)
}
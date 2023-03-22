package com.example.fixturedetails.navigation

import com.example.model.team.Team

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface FixtureDetailsNavigator {
    fun openTeamDetails(team: Team, leagueId: Int, season: Int)
    fun openFixtureDetails(fixtureId: Int)
    fun openPlayerDetails(playerId: Int, team: Team, season: Int)
    fun navigateUp()
}
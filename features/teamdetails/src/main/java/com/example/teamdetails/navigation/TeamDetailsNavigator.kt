package com.example.teamdetails.navigation

import com.example.model.team.Team

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface TeamDetailsNavigator {
    fun openPlayerDetails(playerId: Int, team: Team, season: Int)
    fun openFixtureDetails(fixtureId: Int)
    fun navigateUp()
}
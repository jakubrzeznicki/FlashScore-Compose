package com.example.home.navigation

import com.example.model.league.League

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface HomeNavigator {
    fun openFixtureDetails(fixtureId: Int)
    fun openLeagueDetails(league: League)
    fun openNotifications()
}
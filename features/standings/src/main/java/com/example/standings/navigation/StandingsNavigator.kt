package com.example.standings.navigation

import com.example.model.league.League

/**
 * Created by jrzeznicki on 16/03/2023.
 */
interface StandingsNavigator {
    fun openStandingsDetails(league: League)
    fun openLeagueDetails(league: League)
}
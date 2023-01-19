package com.kuba.flashscorecompose.home.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.FixtureItem
import com.kuba.flashscorecompose.data.league.model.League

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class LeagueFixturesData(val league: League, val fixtures: List<FixtureItem>) {
    companion object {
        val EMPTY_LEAGUE_FIXTURES_DATA = LeagueFixturesData(League.EMPTY_LEAGUE, listOf())
    }
}
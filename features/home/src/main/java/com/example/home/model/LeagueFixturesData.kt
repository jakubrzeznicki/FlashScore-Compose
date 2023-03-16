package com.example.home.model

import com.example.model.fixture.FixtureItemWrapper
import com.example.model.league.League

/**
 * Created by jrzeznicki on 19/01/2023.
 */
data class LeagueFixturesData(val league: League, val fixtureWrappers: List<FixtureItemWrapper>)

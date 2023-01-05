package com.kuba.flashscorecompose.data.fixtures.lineups.model

import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team

data class Lineup(
    val id: Int,
    val fixtureId: Int,
    val coach: Coach,
    val formation: String,
    val startXI: List<Player>,
    val substitutes: List<Player>,
    val team: Team
)
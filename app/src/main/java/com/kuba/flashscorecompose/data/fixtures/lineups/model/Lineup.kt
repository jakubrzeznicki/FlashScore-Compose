package com.kuba.flashscorecompose.data.fixtures.lineups.model

import com.kuba.flashscorecompose.data.team.information.model.Team
import com.kuba.flashscorecompose.data.team.information.model.Coach

data class Lineup(
    val teamId: Int,
    val fixtureId: Int,
    val coach: Coach,
    val formation: String,
    val startXI: List<Player>,
    val substitutes: List<Player>,
    val team: Team,
    val startXIWithPosition: Map<Int, List<Player>>
) {
    companion object {
        val EMPTY_LINEUP =
            Lineup(0, 0, Coach.EMPTY_COACH, "", listOf(), listOf(), Team.EMPTY_TEAM, mapOf())
    }
}
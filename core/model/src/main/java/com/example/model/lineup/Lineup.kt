package com.example.model.lineup

import com.example.model.player.Player
import com.example.model.team.Coach
import com.example.model.team.Team

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

package com.example.database.fixtures.lineups.mapper

import com.example.database.fixtures.lineups.model.LineupEntity
import com.example.database.players.mapper.toPlayer
import com.example.database.teams.mapper.toCoach
import com.example.database.teams.mapper.toTeam
import com.example.model.lineup.Lineup

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun LineupEntity.toLineup(): Lineup {
    return Lineup(
        coach = coach.toCoach(),
        formation = formation,
        startXI = startXI.map { it.toPlayer() },
        substitutes = substitutes.map { it.toPlayer() },
        team = team.toTeam(),
        teamId = teamId,
        fixtureId = fixtureId,
        startXIWithPosition = emptyMap()
    )
}

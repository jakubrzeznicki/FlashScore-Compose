package com.example.data.lineup.mapper

import com.example.data.player.mapper.toPlayer
import com.example.data.player.mapper.toPlayerEntity
import com.example.data.team.mapper.toCoach
import com.example.data.team.mapper.toCoachEntity
import com.example.data.team.mapper.toTeam
import com.example.data.team.mapper.toTeamEntity
import com.example.database.fixtures.lineups.model.LineupEntity
import com.example.model.lineup.Lineup
import com.example.model.player.Player
import com.example.model.team.Coach
import com.example.model.team.Team
import com.example.network.model.lineup.LineupDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
fun Lineup.toLineupEntity(): LineupEntity {
    return LineupEntity(
        teamId = team.id,
        fixtureId = fixtureId,
        coach = coach.toCoachEntity(),
        formation = formation,
        startXI = startXI.map { it.toPlayerEntity() },
        substitutes = substitutes.map { it.toPlayerEntity() },
        team = team.toTeamEntity()
    )
}

fun LineupDto.toLineup(fixtureId: Int, leagueId: Int, season: Int): Lineup {
    return Lineup(
        teamId = team?.id ?: 0,
        fixtureId = fixtureId,
        coach = coach?.toCoach(team?.id ?: 0) ?: Coach.EMPTY_COACH,
        formation = formation.orEmpty(),
        startXI = startXI?.map {
            it.player?.toPlayer(team?.toTeam() ?: Team.EMPTY_TEAM, season) ?: Player.EMPTY_PLAYER
        }.orEmpty(),
        substitutes = substitutes?.map {
            it.player?.toPlayer(team?.toTeam() ?: Team.EMPTY_TEAM, season) ?: Player.EMPTY_PLAYER
        }.orEmpty(),
        team = team?.toTeam(leagueIdParam = leagueId, seasonParam = season) ?: Team.EMPTY_TEAM,
        emptyMap()
    )
}

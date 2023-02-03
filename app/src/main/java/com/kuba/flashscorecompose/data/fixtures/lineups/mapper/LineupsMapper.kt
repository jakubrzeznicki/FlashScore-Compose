package com.kuba.flashscorecompose.data.fixtures.lineups.mapper
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDto
import com.kuba.flashscorecompose.data.players.mapper.toPlayer
import com.kuba.flashscorecompose.data.players.mapper.toPlayerEntity
import com.kuba.flashscorecompose.data.team.information.mapper.toCoach
import com.kuba.flashscorecompose.data.team.information.mapper.toCoachEntity
import com.kuba.flashscorecompose.data.team.information.mapper.toTeam
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.team.information.model.Coach
import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 03/01/2023.
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

fun LineupDto.toLineup(fixtureId: Int): Lineup {
    return Lineup(
        teamId = team?.id ?: 0,
        fixtureId = fixtureId,
        coach = coach?.toCoach(team?.id ?: 0) ?: Coach.EMPTY_COACH,
        formation = formation.orEmpty(),
        startXI = startXI?.map { it.player.toPlayer() }.orEmpty(),
        substitutes = substitutes?.map { it.player.toPlayer() }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM,
        emptyMap()
    )
}
package com.kuba.flashscorecompose.data.fixtures.lineups.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeam
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.fixtures.fixture.model.Team
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.CoachEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Coach
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Player
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.CoachDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.PlayerDto
import java.util.*

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun LineupEntity.toLineup(): Lineup {
    return Lineup(
        teamId = teamId,
        fixtureId = fixtureId,
        coach = coach.toCoach(),
        formation = formation,
        startXI = startXI.map { it.toPlayer() },
        substitutes = substitutes.map { it.toPlayer() },
        team = team.toTeam()
    )
}

fun CoachEntity.toCoach(): Coach {
    return Coach(id = id ?: 0, teamId = teamId, name = name.orEmpty(), photo = photo.orEmpty())
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        grid = grid.orEmpty(),
        id = id ?: 0,
        teamId = teamId,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}

fun Lineup.toLineupEntity(): LineupEntity {
    return LineupEntity(
        teamId = teamId,
        fixtureId = fixtureId,
        coach = coach.toCoachEntity(),
        formation = formation,
        startXI = startXI.map { it.toPlayerEntity() },
        substitutes = substitutes.map { it.toPlayerEntity() },
        team = team.toTeamEntity()
    )
}

fun Coach.toCoachEntity(): CoachEntity {
    return CoachEntity(
        id = id ?: 0,
        teamId = teamId,
        name = name.orEmpty(),
        photo = photo.orEmpty(),
        "",
        "",
        9,
        ""
    )
}

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        grid = grid.orEmpty(),
        teamId = teamId,
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}

fun LineupDto.toLineup(fixtureId: Int): Lineup {
    return Lineup(
        teamId = team?.id ?: 0,
        fixtureId = fixtureId,
        coach = coach?.toCoach(team?.id ?: 0) ?: Coach.EMPTY_COACH,
        formation = formation.orEmpty(),
        startXI = startXI?.map { it.player.toPlayer(team?.id ?: 0) }.orEmpty(),
        substitutes = substitutes?.map { it.player.toPlayer(team?.id ?: 0) }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM
    )
}

fun CoachDto.toCoach(teamId: Int): Coach {
    return Coach(id = id ?: 0, teamId = teamId, name = name.orEmpty(), photo = photo.orEmpty())
}

fun PlayerDto.toPlayer(teamId: Int): Player {
    return Player(
        grid = grid.orEmpty(),
        teamId = teamId,
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}
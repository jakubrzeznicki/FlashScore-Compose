package com.kuba.flashscorecompose.data.fixtures.lineups.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeam
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
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
        uuid = uuid,
        fixtureId = fixtureId,
        coach = coach.toCoach(),
        formation = formation,
        startXI = startXI.map { it.toPlayer() },
        substitutes = substitutes.map { it.toPlayer() },
        team = team.toTeam()
    )
}

fun CoachEntity.toCoach(): Coach {
    return Coach(id = id ?: 0, name = name.orEmpty(), photo = photo.orEmpty())
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        grid = grid.orEmpty(),
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}

fun Lineup.toLineupEntity(): LineupEntity {
    return LineupEntity(
        uuid = uuid,
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
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}

fun LineupDto.toLineup(fixtureId: Int): Lineup {
    return Lineup(
        uuid = UUID.randomUUID().toString(),
        fixtureId = fixtureId,
        coach = coach.toCoach(),
        formation = formation,
        startXI = startXI.map { it.toPlayer() },
        substitutes = substitutes.map { it.toPlayer() },
        team = team.toTeam()
    )
}

fun CoachDto.toCoach(): Coach {
    return Coach(id = id ?: 0, name = name.orEmpty(), photo = photo.orEmpty())
}

fun PlayerDto.toPlayer(): Player {
    return Player(
        grid = grid.orEmpty(),
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty()
    )
}
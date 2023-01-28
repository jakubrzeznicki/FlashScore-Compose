package com.kuba.flashscorecompose.data.fixtures.lineups.mapper
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.BirthEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.LineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.local.model.PlayerEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Birth
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Player
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.BirthDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDto
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.PlayerDto
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

fun BirthEntity.toBirth(): Birth {
    return Birth(date = date, place = place, country = country)
}

fun PlayerEntity.toPlayer(): Player {
    return Player(
        grid = grid,
        id = id,
        name = name,
        number = number,
        pos = pos,
        firstname = firstname,
        lastname = lastname,
        age = age,
        position = position,
        birth = birth.toBirth(),
        nationality = nationality,
        height = height,
        weight = weight,
        injured = injured,
        photo = photo,
        teamId = teamId
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
        team = team.toTeamEntity(null)
    )
}

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        grid = grid,
        teamId = teamId,
        id = id,
        name = name,
        number = number,
        pos = pos,
        firstname = firstname,
        lastname = lastname,
        age = age,
        position = position,
        birth = birth.toBirthEntity(),
        nationality = nationality,
        height = height,
        weight = weight,
        injured = injured,
        photo = photo
    )
}

fun Birth.toBirthEntity(): BirthEntity {
    return BirthEntity(date = date, place = place, country = country)
}

fun LineupDto.toLineup(fixtureId: Int): Lineup {
    return Lineup(
        teamId = team?.id ?: 0,
        fixtureId = fixtureId,
        coach = coach?.toCoach(team?.id ?: 0) ?: Coach.EMPTY_COACH,
        formation = formation.orEmpty(),
        startXI = startXI?.map { it.player.toPlayer(team?.id ?: 0) }.orEmpty(),
        substitutes = substitutes?.map { it.player.toPlayer(team?.id ?: 0) }.orEmpty(),
        team = team?.toTeam() ?: Team.EMPTY_TEAM,
        emptyMap()
    )
}

fun PlayerDto.toPlayer(teamId: Int): Player {
    return Player(
        grid = grid.orEmpty(),
        teamId = teamId,
        id = id ?: 0,
        name = name.orEmpty(),
        number = number ?: 0,
        pos = pos.orEmpty(),
        firstname = firstname.orEmpty(),
        lastname = lastname.orEmpty(),
        age = age ?: 0,
        position = position.orEmpty(),
        birth = birth?.toBirth() ?: Birth.EMPTY_BIRTH,
        nationality = nationality.orEmpty(),
        height = height.orEmpty(),
        weight = weight.orEmpty(),
        injured = injured ?: false,
        photo = photo.orEmpty()
    )
}

fun BirthDto.toBirth(): Birth {
    return Birth(date = date.orEmpty(), place = place.orEmpty(), country = country.orEmpty())
}
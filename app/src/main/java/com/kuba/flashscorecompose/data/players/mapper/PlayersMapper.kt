package com.kuba.flashscorecompose.data.players.mapper

import com.kuba.flashscorecompose.data.players.local.model.BirthEntity
import com.kuba.flashscorecompose.data.players.local.model.PlayerEntity
import com.kuba.flashscorecompose.data.players.model.Birth
import com.kuba.flashscorecompose.data.players.model.Player
import com.kuba.flashscorecompose.data.players.remote.model.BirthDto
import com.kuba.flashscorecompose.data.players.remote.model.PlayerDto
import com.kuba.flashscorecompose.data.team.information.mapper.toTeam
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.team.information.model.Team

/**
 * Created by jrzeznicki on 29/01/2023.
 */

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
        team = team.toTeam(),
        season = season
    )
}

fun Player.toPlayerEntity(): PlayerEntity {
    return PlayerEntity(
        grid = grid,
        team = team.toTeamEntity(),
        season = season,
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


fun PlayerDto.toPlayer(team: Team, season: Int): Player {
    return Player(
        grid = grid.orEmpty(),
        team = team,
        season = season,
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


fun PlayerDto.toPlayer(): Player {
    return Player(
        grid = grid.orEmpty(),
        team = Team.EMPTY_TEAM,
        season = 0,
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
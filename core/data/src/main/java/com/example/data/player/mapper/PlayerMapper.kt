package com.example.data.player.mapper

import com.example.data.team.mapper.toTeamEntity
import com.example.database.players.model.BirthEntity
import com.example.database.players.model.PlayerEntity
import com.example.model.player.Birth
import com.example.model.player.Player
import com.example.model.team.Team
import com.example.network.model.player.BirthDto
import com.example.network.model.player.PlayerDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */
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
    return BirthEntity(
        date = date,
        place = place,
        country = country
    )
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

fun BirthDto.toBirth(): Birth {
    return Birth(date = date.orEmpty(), place = place.orEmpty(), country = country.orEmpty())
}

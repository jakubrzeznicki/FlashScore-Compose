package com.example.database.players.mapper

import com.example.database.players.model.BirthEntity
import com.example.database.players.model.PlayerEntity
import com.example.database.teams.mapper.toTeam
import com.example.model.player.Birth
import com.example.model.player.Player

/**
 * Created by jrzeznicki on 13/03/2023.
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
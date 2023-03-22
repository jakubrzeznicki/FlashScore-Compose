package com.example.data.league.mapper

import com.example.database.leagues.model.LeagueEntity
import com.example.model.league.League
import com.example.network.model.league.LeagueDto

/**
 * Created by jrzeznicki on 14/03/2023.
 */

fun League.toLeagueEntity(): LeagueEntity {
    return LeagueEntity(
        id = id,
        name = name,
        type = type,
        logo = logo,
        countryCode = countryCode,
        countryName = countryName,
        countryFlag = countryFlag,
        round = round,
        season = season
    )
}

fun LeagueDto.toLeague(): League {
    return League(
        id = id ?: 0,
        name = name.orEmpty(),
        type = type.orEmpty(),
        logo = logo.orEmpty(),
        countryCode = "",
        countryName = countryName.orEmpty(),
        countryFlag = flag.orEmpty(),
        round = round.orEmpty(),
        season = season ?: 2023
    )
}

package com.example.database.leagues.mapper

import com.example.database.leagues.model.LeagueEntity
import com.example.model.league.League

/**
 * Created by jrzeznicki on 13/03/2023.
 */
fun LeagueEntity.toLeague(): League {
    return League(
        id = id,
        name = name,
        type = type,
        logo = logo,
        countryCode = countryCode,
        countryName = countryName,
        countryFlag = countryFlag,
        season = season,
        round = round
    )
}
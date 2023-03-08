package com.kuba.flashscorecompose.data.league.mapper

import com.kuba.flashscorecompose.data.fixtures.fixture.remote.model.LeagueDto
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import com.kuba.flashscorecompose.data.league.model.League

/**
 * Created by jrzeznicki on 21/12/2022.
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

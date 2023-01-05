package com.kuba.flashscorecompose.data.league.mapper

import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.data.league.remote.model.LeagueDto

/**
 * Created by jrzeznicki on 21/12/2022.
 */
fun LeagueEntity.toLeague(): League {
    return League(id = id, name = name, type = type, logo = logo, countryCode = countryCode)
}

fun League.toLeagueEntity(): LeagueEntity {
    return LeagueEntity(id = id, name = name, type = type, logo = logo, countryCode = countryCode)
}

fun LeagueDto.toLeague(): League {
    return League(
        id = id ?: 0,
        name = name.orEmpty(),
        type = type.orEmpty(),
        logo = logo.orEmpty(),
        countryCode = ""
    )
}
package com.kuba.flashscorecompose.data.fixtures.currentround.mapper

import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity
import com.kuba.flashscorecompose.data.fixtures.currentround.model.CurrentRound
import com.kuba.flashscorecompose.data.fixtures.currentround.remote.model.CurrentRoundDataDto

/**
 * Created by jrzeznicki on 03/01/2023.
 */
fun CurrentRoundEntity.toCurrentRound(): CurrentRound {
    return CurrentRound(id = id, leagueId = leagueId, season = season, round = round)
}

fun CurrentRound.toCurrentRoundEntity(): CurrentRoundEntity {
    return CurrentRoundEntity(id = id, leagueId = leagueId, season = season, round = round)
}

fun CurrentRoundDataDto.toCurrentRound(): CurrentRound {
    return CurrentRound(
        id = 0,
        leagueId = parameters?.league ?: 0,
        season = parameters?.season ?: 0,
        round = response?.first()?.round.orEmpty()
    )
}
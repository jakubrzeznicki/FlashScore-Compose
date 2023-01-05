package com.kuba.flashscorecompose.data.fixtures.currentround.local

import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface CurrentRoundLocalDataSource {
    fun getCurrentRound(leagueId: Int): CurrentRoundEntity
    fun saveCurrentRound(currentRound: CurrentRoundEntity)
    fun deleteCurrentRound(leagueId: Int)
}
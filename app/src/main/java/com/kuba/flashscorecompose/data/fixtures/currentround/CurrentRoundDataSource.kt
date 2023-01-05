package com.kuba.flashscorecompose.data.fixtures.currentround

import com.kuba.flashscorecompose.data.fixtures.currentround.model.CurrentRound
import com.kuba.flashscorecompose.utils.RepositoryResult

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface CurrentRoundDataSource {
    fun getCurrentRound(leagueId: Int): CurrentRound
    fun saveCurrentRound(currentRound: CurrentRound)
    suspend fun loadCurrentRound(leagueId: Int, season: Int): RepositoryResult<CurrentRound>
}
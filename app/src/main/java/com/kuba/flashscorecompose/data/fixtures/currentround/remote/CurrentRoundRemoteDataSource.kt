package com.kuba.flashscorecompose.data.fixtures.currentround.remote

import com.kuba.flashscorecompose.data.fixtures.currentround.remote.model.CurrentRoundDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface CurrentRoundRemoteDataSource {
    suspend fun loadCurrentRound(
        leagueId: Int,
        season: Int,
        isCurrent: Boolean = true
    ): Response<CurrentRoundDataDto>
}
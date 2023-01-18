package com.kuba.flashscorecompose.data.fixtures.lineups.remote

import com.kuba.flashscorecompose.data.fixtures.lineups.remote.model.LineupDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface LineupsRemoteDataSource {
    suspend fun loadLineups(fixtureId: Int): Response<LineupDataDto>
}
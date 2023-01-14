package com.kuba.flashscorecompose.data.fixtures.fixture.remote

import com.kuba.flashscorecompose.api.FootballApi
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.StatisticsRemoteDataSource
import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class StatisticsRemote(private val footballApi: FootballApi) : StatisticsRemoteDataSource {
    override suspend fun loadStatistics(fixtureId: Int): Response<StatisticsDataDto> =
        footballApi.getFixturesStatistics(fixtureId = fixtureId)
}
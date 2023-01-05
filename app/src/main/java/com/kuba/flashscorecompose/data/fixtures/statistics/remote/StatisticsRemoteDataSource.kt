package com.kuba.flashscorecompose.data.fixtures.fixture.remote

import com.kuba.flashscorecompose.data.fixtures.statistics.remote.model.StatisticsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface StatisticsRemoteDataSource {
    suspend fun loadStatistics(fixtureId: Int): Response<StatisticsDataDto>
}
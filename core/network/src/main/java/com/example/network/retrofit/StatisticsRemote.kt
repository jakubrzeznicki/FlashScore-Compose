package com.example.network.retrofit

import com.example.network.StatisticsRemoteDataSource
import com.example.network.api.FootballApi
import com.example.network.model.statistics.StatisticsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class StatisticsRemote(private val footballApi: FootballApi) : StatisticsRemoteDataSource {
    override suspend fun loadStatistics(fixtureId: Int): Response<StatisticsDataDto> =
        footballApi.getFixturesStatistics(fixtureId = fixtureId)
}

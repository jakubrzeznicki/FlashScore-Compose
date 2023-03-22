package com.example.network

import com.example.network.model.statistics.StatisticsDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface StatisticsRemoteDataSource {
    suspend fun loadStatistics(fixtureId: Int): Response<StatisticsDataDto>
}

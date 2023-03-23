package com.example.network

import com.example.network.model.lineup.LineupDataDto
import retrofit2.Response

/**
 * Created by jrzeznicki on 03/01/2023.
 */
interface LineupsRemoteDataSource {
    suspend fun loadLineups(fixtureId: Int): Response<LineupDataDto>
}

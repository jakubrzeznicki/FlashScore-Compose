package com.kuba.flashscorecompose.data.league.remote

import com.kuba.flashscorecompose.api.FlashScoreApi
import com.kuba.flashscorecompose.data.league.model.League
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 10/1/2022
 */
class LeagueRemote(private val flashScoreApi: FlashScoreApi) : LeagueRemoteDataSource {
    override suspend fun loadLeagues(countryId: String): RepositoryResult<List<League>> {
        val result = flashScoreApi.getLeagues(countryId = countryId)
        return try {
            RepositoryResult.Success(result.body().orEmpty())
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
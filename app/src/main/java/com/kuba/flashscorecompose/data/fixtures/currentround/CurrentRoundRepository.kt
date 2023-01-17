package com.kuba.flashscorecompose.data.fixtures.currentround

import com.kuba.flashscorecompose.data.fixtures.currentround.local.CurrentRoundLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.currentround.mapper.toCurrentRound
import com.kuba.flashscorecompose.data.fixtures.currentround.mapper.toCurrentRoundEntity
import com.kuba.flashscorecompose.data.fixtures.currentround.model.CurrentRound
import com.kuba.flashscorecompose.data.fixtures.currentround.remote.CurrentRoundRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class CurrentRoundRepository(
    private val local: CurrentRoundLocalDataSource,
    private val remote: CurrentRoundRemoteDataSource
) : CurrentRoundDataSource {
    override fun getCurrentRound(leagueId: Int): CurrentRound =
        local.getCurrentRound(leagueId).toCurrentRound()

    override fun saveCurrentRound(currentRound: CurrentRound) {
        local.saveCurrentRound(currentRound.toCurrentRoundEntity())
    }

    override suspend fun loadCurrentRound(
        leagueId: Int,
        season: Int
    ): RepositoryResult<CurrentRound> {
        val result = remote.loadCurrentRound(leagueId, season)
        return try {
            val currentRound = result.body()?.toCurrentRound()
            withContext(Dispatchers.IO) {
                currentRound?.let {
                    local.deleteCurrentRound(it.leagueId)
                    saveCurrentRound(currentRound)
                }
            }
            RepositoryResult.Success(currentRound)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
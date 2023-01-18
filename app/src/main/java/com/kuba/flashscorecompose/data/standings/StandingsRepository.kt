package com.kuba.flashscorecompose.data.standings

import com.kuba.flashscorecompose.data.standings.local.StandingsLocalDataSource
import com.kuba.flashscorecompose.data.standings.mapper.toStandings
import com.kuba.flashscorecompose.data.standings.mapper.toStandingsEntity
import com.kuba.flashscorecompose.data.standings.model.Standings
import com.kuba.flashscorecompose.data.standings.remote.StandingsRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsRepository(
    private val local: StandingsLocalDataSource,
    private val remote: StandingsRemoteDataSource
) : StandingsDataSource {
    override fun observeStandings(leagueId: Int, season: Int): Flow<List<Standings>> {
        return local.observeStandings(leagueId, season).map { standingsEntity ->
            standingsEntity.map { it.toStandings() }
        }
    }

    override suspend fun saveStandings(standings: List<Standings>) {
        local.saveStandings(standings.map { it.toStandingsEntity() })
    }

    override suspend fun loadStandings(
        leagueId: Int,
        season: Int
    ): RepositoryResult<List<Standings>> {
        val result = remote.loadStandings(leagueId, season)
        return try {
            val standings = result.body()?.response?.map { leagueStandingsDto ->
                leagueStandingsDto.league?.toStandings() ?: Standings.EMPTY_STANDINGS
            }.orEmpty()
            saveStandings(standings = standings)
            RepositoryResult.Success(standings)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
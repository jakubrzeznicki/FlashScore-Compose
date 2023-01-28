package com.kuba.flashscorecompose.data.standings

import com.kuba.flashscorecompose.data.league.mapper.toLeagueEntity
import com.kuba.flashscorecompose.data.standings.local.StandingsLocalDataSource
import com.kuba.flashscorecompose.data.standings.mapper.toStandings
import com.kuba.flashscorecompose.data.standings.mapper.toStandingsEntity
import com.kuba.flashscorecompose.data.standings.model.Standing
import com.kuba.flashscorecompose.data.standings.remote.StandingsRemoteDataSource
import com.kuba.flashscorecompose.data.team.information.mapper.toTeamEntity
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 18/01/2023.
 */
class StandingsRepository(
    private val local: StandingsLocalDataSource,
    private val remote: StandingsRemoteDataSource
) : StandingsDataSource {
    override fun observeStandings(leagueIds: List<Int>, season: Int): Flow<List<Standing>> {
        return local.observeStandings(leagueIds, season).map { standingsEntity ->
            standingsEntity.map { it.toStandings() }
        }
    }

    override suspend fun getStanding(leagueId: Int, season: Int): Standing {
        return local.getStanding(leagueId, season).toStandings()
    }

    override suspend fun saveStandings(standings: List<Standing>) {
        local.saveStandings(standings.map { it.toStandingsEntity() })
    }

    override suspend fun loadStandings(
        leagueId: Int,
        season: Int
    ): RepositoryResult<List<Standing>> {
        val result = remote.loadStandings(leagueId, season)
        return try {
            val standings = result.body()?.response?.map { leagueStandingsDto ->
                leagueStandingsDto.league?.toStandings() ?: Standing.EMPTY_Standing
            }.orEmpty()
            withContext(Dispatchers.IO) {
                saveStandings(standings = standings)
                local.saveLeagues(standings.map { it.league.toLeagueEntity() })
                val standingItemsWithLeague = standings.map { it.leagueId to it.standingItems }
                standingItemsWithLeague.forEach {
                    val teams =
                        it.second.map { standingItem -> standingItem.team.toTeamEntity(it.first) }
                    local.saveTeams(teams)
                }
            }
            RepositoryResult.Success(standings)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
package com.kuba.flashscorecompose.data.fixtures.lineups

import android.util.Log
import com.kuba.flashscorecompose.data.fixtures.fixture.mapper.toTeamEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.LineupsRemoteDataSource
import com.kuba.flashscorecompose.data.fixtures.lineups.local.LineupLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toCoachEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toLineup
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toLineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toPlayerEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class LineupsRepository(
    private val local: LineupLocalDataSource,
    private val remote: LineupsRemoteDataSource
) : LineupsDataSource {

    override fun observeLineups(fixtureId: Int): Flow<List<Lineup>> {
        return local.observeLineups(fixtureId).map { lineups ->
            lineups.map { it.toLineup() }
        }
    }

    override suspend fun saveLineups(lineups: List<Lineup>) {
        local.saveLineups(lineups.map { it.toLineupEntity() })
    }

    override suspend fun loadLineups(fixtureId: Int): RepositoryResult<List<Lineup>> {
        val result = remote.loadLineups(fixtureId = fixtureId)
        return try {
            val lineups = result.body()?.response?.map { it.toLineup(fixtureId) }
            saveLineups(lineups.orEmpty())
            local.saveCoaches(lineups?.map { it.coach.toCoachEntity() }.orEmpty())
            local.saveTeams(lineups?.map { it.team.toTeamEntity() }.orEmpty())
            val startXI = lineups?.map {
                it.startXI.map { player -> player.toPlayerEntity() }
            }.orEmpty().flatten()
            val substitutions = lineups?.map {
                it.substitutes.map { player -> player.toPlayerEntity() }
            }.orEmpty().flatten()
            local.savePlayers(startXI + substitutions)
            RepositoryResult.Success(lineups)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
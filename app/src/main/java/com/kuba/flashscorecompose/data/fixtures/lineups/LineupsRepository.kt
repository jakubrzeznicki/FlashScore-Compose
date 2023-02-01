package com.kuba.flashscorecompose.data.fixtures.lineups

import com.kuba.flashscorecompose.data.fixtures.lineups.local.LineupLocalDataSource
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toLineup
import com.kuba.flashscorecompose.data.fixtures.lineups.mapper.toLineupEntity
import com.kuba.flashscorecompose.data.fixtures.lineups.model.Lineup
import com.kuba.flashscorecompose.data.fixtures.lineups.remote.LineupsRemoteDataSource
import com.kuba.flashscorecompose.utils.RepositoryResult
import com.kuba.flashscorecompose.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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
            withContext(Dispatchers.IO) {
                saveLineups(lineups.orEmpty())
            }
            RepositoryResult.Success(lineups)
        } catch (e: HttpException) {
            RepositoryResult.Error(ResponseStatus().apply {
                this.statusMessage = e.message()
                this.internalStatus = e.code()
            })
        }
    }
}
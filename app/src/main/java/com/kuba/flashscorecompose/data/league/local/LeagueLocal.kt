package com.kuba.flashscorecompose.data.league.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.league.local.model.LeagueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
class LeagueLocal(private val roomStorage: RoomStorage) : LeagueLocalDataSource {
    override fun observeLeagues(): Flow<List<LeagueEntity>> {
        return roomStorage.getDatabase().leagueDao().observeLeagues()
    }

    override suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity> {
        return roomStorage.getDatabase().leagueDao().getLeagues(countryNames)
    }
}
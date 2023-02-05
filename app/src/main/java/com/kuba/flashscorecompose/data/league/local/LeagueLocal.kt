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

    override fun observeLeagues(countryCode: String): Flow<List<LeagueEntity>> {
        return roomStorage.getDatabase().leagueDao().observeLeagues(countryCode)
    }

    override fun observeLeagues(countryCodes: List<String>): Flow<List<LeagueEntity>> {
        return roomStorage.getDatabase().leagueDao().observeLeagues(countryCodes)
    }

    override fun observeLeague(id: Int): Flow<LeagueEntity?> {
        return roomStorage.getDatabase().leagueDao().getLeagueById(id)
    }

    override suspend fun getLeagues(countryNames: List<String>): List<LeagueEntity> {
        return roomStorage.getDatabase().leagueDao().getLeagues(countryNames)
    }

    override fun saveLeagues(leagues: List<LeagueEntity>) {
        roomStorage.getDatabase().leagueDao().saveLeagues(leagues)
    }

    override fun deleteLeagues(countryCode: String) {
        roomStorage.getDatabase().leagueDao().deleteLeagues(countryCode)
    }
}
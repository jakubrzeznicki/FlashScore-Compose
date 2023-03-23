package com.example.data.league.local

import com.example.database.RoomStorage
import com.example.database.leagues.model.LeagueEntity
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

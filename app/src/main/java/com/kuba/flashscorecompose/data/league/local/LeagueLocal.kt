package com.kuba.flashscorecompose.data.league.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.league.model.League
import kotlinx.coroutines.flow.Flow

/**
 * Created by jrzeznicki on 10/1/2022
 */
class LeagueLocal(private val roomStorage: RoomStorage) : LeagueLocalDataSource {
    override fun observeLeagues(countryId: String): Flow<List<League>> {
        return roomStorage.getDatabase().leagueDao().observeLeagues(countryId)
    }

    override fun saveLeagues(leagues: List<League>) {
        roomStorage.getDatabase().leagueDao().saveLeagues(leagues)
    }

    override fun deleteLeagues(countryId: String) {
        roomStorage.getDatabase().leagueDao().deleteLeagues(countryId)
    }
}
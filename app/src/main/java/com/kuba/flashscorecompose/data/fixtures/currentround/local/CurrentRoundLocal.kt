package com.kuba.flashscorecompose.data.fixtures.currentround.local

import com.kuba.flashscorecompose.data.RoomStorage
import com.kuba.flashscorecompose.data.fixtures.currentround.local.model.CurrentRoundEntity

/**
 * Created by jrzeznicki on 03/01/2023.
 */
class CurrentRoundLocal(private val roomStorage: RoomStorage) : CurrentRoundLocalDataSource {
    override fun getCurrentRound(leagueId: Int): CurrentRoundEntity {
        return roomStorage.getDatabase().currentRound().getCurrentRound(leagueId)
    }

    override fun saveCurrentRound(currentRound: CurrentRoundEntity) {
        roomStorage.getDatabase().currentRound().saveCurrentRound(currentRound)
    }

    override fun deleteCurrentRound(leagueId: Int) {
        roomStorage.getDatabase().currentRound().deleteCurrentRound(leagueId)
    }
}